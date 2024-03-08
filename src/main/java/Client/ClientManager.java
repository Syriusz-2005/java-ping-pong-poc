package Client;

import Renderer.WindowRenderer;
import Server.GameState;
import jakarta.websocket.Session;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public final SceneManager sceneManager = new SceneManager();
    public final WindowRenderer renderer = new WindowRenderer();
    private GameState gameState;

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
    }
}
