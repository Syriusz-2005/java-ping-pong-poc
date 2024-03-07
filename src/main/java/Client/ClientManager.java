package Client;

import Renderer.SceneRenderer;
import Server.GameState;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public final SceneManager sceneManager = new SceneManager();
    public final SceneRenderer renderer = new SceneRenderer();
    private GameState gameState;

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
    }
}
