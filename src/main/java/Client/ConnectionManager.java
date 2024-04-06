package Client;

import Message.CommandType;
import Message.MessageType;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ConnectionManager {
    private final Session session;
    private final RemoteEndpoint.Async remote;
    public ConnectionManager(Session session) {
        this.session = session;
        remote = session.getAsyncRemote();
    }

    public Future<Void> postMessage(MessageType msg) {
        return remote.sendObject(msg);
    }

    public Future<Void> leaveGame() {
        var msg = new MessageType().setCommand(CommandType.LEAVE_GAME);
        return remote.sendObject(msg);
    }
}
