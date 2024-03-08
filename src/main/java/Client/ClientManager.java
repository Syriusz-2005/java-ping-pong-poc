package Client;

import Message.CommandType;
import Message.MessageType;
import Renderer.WindowRenderer;
import Server.GameState;
import jakarta.websocket.Session;

import java.awt.event.ActionEvent;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public final SceneManager sceneManager = new SceneManager();

    public final WindowRenderer renderer = new WindowRenderer(this);
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
            renderer.displayLobbyMenu();
        }
    }

    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
    }

    public void createGame() {
        var msg = new MessageType().setCommand(CommandType.CREATE_GAME);
        connectionManager.postMessage(msg);
    }
}
