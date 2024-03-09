package Server;

import Message.CommandType;
import Message.MessageType;
import Physics.PhysicsScene;
import Physics.PhysicsSceneConfig;
import Physics.Rectangle;
import Utils.LogLevel;
import Utils.Logger;
import Vector.MutFVec2;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameLoop extends Thread {
    public static float SCENE_WIDTH = 1600;
    public static float SCENE_HEIGHT = 800;

    private int stepIndex = 0;
    private int preparingTicks = 0;
    private final Game game;
    private final GameLoopConfig config;

    private final PhysicsScene scene = new PhysicsScene(PhysicsScene.DEFAULT_SCENE_CONFIG);

    private Rectangle ceiling;
    private Rectangle player0Palette;
    private Rectangle ball;


    public GameLoop(Game game, GameLoopConfig config) {
        super();
        this.game = game;
        this.config = config;
    }

    public void dispose() {
        scene.removeAll();
    }

    public void initializeScene() {
        dispose();

        float sceneWidth = SCENE_WIDTH;
        float sceneHeight = SCENE_HEIGHT;

        player0Palette = new Rectangle(30, 120, "player0");
        player0Palette.isImmovable = true;
        player0Palette.setPos(new MutFVec2(20, sceneHeight / 2));

        var player1Palette = new Rectangle(30, 120, "player1");
        player1Palette.isImmovable = true;
        player1Palette.setPos(new MutFVec2(sceneWidth - 20, sceneHeight / 2));

        scene.add(player0Palette);
        scene.add(player1Palette);

        ceiling = new Rectangle(sceneWidth, sceneHeight, "ceiling");
        ceiling.setPos(new MutFVec2(sceneWidth / 2,sceneHeight * 1.5f));
        ceiling.isImmovable = true;

        var floor = new Rectangle(sceneWidth, sceneHeight, "floor");
        floor.setPos(new MutFVec2(sceneWidth / 2,-sceneHeight / 2));
        floor.isImmovable = true;

        scene.add(ceiling);
        scene.add(floor);

        ball = new Rectangle(40, 40, "ball");
        ball.getPos().setX(sceneWidth / 2).setY(sceneHeight / 2);
        scene.add(ball);
    }

    /**
     * Run when the game enters simulation stage.
     */
    private void initializeSimulation() {
        ball.getVelocity().setX(4);
    }

    private void processLazyStep() {
        game.broadcastMetadata();
    }

    private void processPacketStep() {
        var objects = scene.getObjects();
        Rectangle[] arr = new Rectangle[objects.size()];
        objects.toArray(arr);
        game.broadcastSceneState(arr);
    }

    private void processStep() {
        stepIndex++;
        var gameState = game.getState();
        boolean isLazy = stepIndex % config.simulationStepsPerSecond() == 0;
        if (isLazy) {
            processLazyStep();
        }

        if (gameState == GameState.PREPARING) {
            preparingTicks++;
            if (preparingTicks >= config.preparationTimeInTicks()) {
                game.setState(GameState.SIMULATING);
                initializeSimulation();
            }
        }

        if (gameState == GameState.PREPARING || gameState == GameState.SIMULATING) {
            float stepLength = 100f / config.simulationStepsPerSecond();
            scene.step(stepLength);

            // Reducing the amount of update packets during preparation stage
            var simulationStepsToPacketSend = (config.simulationStepsToPacketsRatio()) + (gameState == GameState.PREPARING ? 10 : 0);

            boolean isPacketStep = stepIndex % simulationStepsToPacketSend == 0;
            if (isPacketStep) {
                processPacketStep();
            }
        }

    }


    @Override
    public void run() {
        Logger.print("Started new game loop simulation thread", LogLevel.SPECIFIC);
        while (!game.isDisposed.get()) {
            int stepsPerSecond = config.simulationStepsPerSecond();
            int sleepTime = 1000 / stepsPerSecond;
            processStep();

            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void logState() {
        var objects = scene.getObjects();
        for (var o : objects) {
            System.out.println(o);
        }
    }
    public void moveAllObjects(MutFVec2 vec) {
        for (var o : scene.getObjects()) {
            o.getPos().add(vec);
        }
    }

    public void reinit() {
        scene.removeAll();
        initializeScene();
        initializeSimulation();
    }
}
