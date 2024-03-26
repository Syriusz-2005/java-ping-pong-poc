package Client;

import Message.CommandType;
import Message.MessageType;
import Renderer.GameSceneRenderer;
import Renderer.WindowRenderer;
import Server.GameState;
import jakarta.websocket.Session;
import org.lwjgl.glfw.GLFWKeyCallback;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public final SceneManager sceneManager = new SceneManager();

    public final WindowRenderer windowRenderer = new WindowRenderer(this);
    public final GLFWKeyCallback onKey = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            char c = (char) key;
            var msg = new MessageType().setCommand(CommandType.KEY_STATE_UPDATE);
            msg.data.keyState.paletteMovement = switch (c) {
                case 'W' -> 1;
                case 'S' -> -1;
                default -> 0;
            };
            connectionManager.postMessage(msg);
        }
    };
    public final GameSceneRenderer sceneRenderer = new GameSceneRenderer(this.sceneManager.scene, onKey);
    private GameState gameState;
    private String gameCode;

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        System.out.print("New state: ");
        System.out.println(gameState);
        if (gameState == GameState.WAITING_IN_LOBBY) {
            windowRenderer.displayWaitingMenu();
        } else if (gameState == GameState.PREPARING) {
            windowRenderer.displayPreparingMenu();
            sceneRenderer.start();
        }
    }

    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
    }

    public void createGame() {
        var msg = new MessageType().setCommand(CommandType.CREATE_GAME);
        connectionManager.postMessage(msg);
    }

    public void joinGame(String gameCode) {
        var msg = new MessageType().setCommand(CommandType.GAME_JOIN_REQUEST);
        msg.data.gameCode = gameCode;
        connectionManager.postMessage(msg);
    }
}
