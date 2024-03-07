package Client;

import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public final SceneManager sceneManager = new SceneManager();

    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
        session.addMessageHandler(new MessageHandler() {
            
        });
    }

    public onMessage() {

    }
}
