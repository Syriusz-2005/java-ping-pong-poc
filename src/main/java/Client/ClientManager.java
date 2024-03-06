package Client;

import jakarta.websocket.Session;

public class ClientManager {
    public final ConnectionManager connectionManager;
    public ClientManager(Session session) {
        this.connectionManager = new ConnectionManager(session);
    }


}
