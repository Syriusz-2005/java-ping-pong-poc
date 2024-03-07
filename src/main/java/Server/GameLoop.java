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
    private int stepIndex = 0;
    private int preparingTicks = 0;
    private final Game game;
    private final GameLoopConfig config;

    private final PhysicsScene scene = new PhysicsScene(PhysicsScene.DEFAULT_SCENE_CONFIG);

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

        float sceneWidth = 1600;
        float sceneHeight = 800;

        var player0Palette = new Rectangle(30, 120);
        player0Palette.isImmovable = true;
        player0Palette.setPos(new MutFVec2(20, sceneHeight / 2));

        var player1Palette = new Rectangle(30, 120);
        player1Palette.isImmovable = true;
        player1Palette.setPos(new MutFVec2(sceneWidth - 20, sceneHeight / 2));

        scene.add(player0Palette);
        scene.add(player1Palette);

        var ceiling = new Rectangle(sceneWidth, sceneHeight);
        ceiling.setPos(new MutFVec2(sceneWidth / 2,-sceneHeight / 2));
        var floor = new Rectangle(sceneWidth, sceneHeight);
        floor.setPos(new MutFVec2(sceneWidth / 2,sceneHeight * 1.5f));
        scene.add(ceiling);
        scene.add(floor);
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
            }
        }

        if (!(gameState == GameState.PREPARING || gameState == GameState.SIMULATING)) {
            return;
        }

        float stepLength = 100f / config.simulationStepsPerSecond();
        scene.step(stepLength);

        boolean isPacketStep = stepIndex % config.simulationStepsToPacketsRatio() == 0;
        if (isPacketStep) {
            processPacketStep();
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
}
