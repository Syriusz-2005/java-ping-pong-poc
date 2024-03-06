package Server;

import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/connect/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class ConnectionEndpoint {

    private Session session;
    private static Set<ConnectionEndpoint> connectionEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, User> users = new HashMap<>();


    private void close(InvalidDataException err) throws IOException {
        users.remove(this.session.getId());
        connectionEndpoints.remove(this);
        session.close(new CloseReason(new CloseReason.CloseCode() {
            @Override
            public int getCode() {
                return 400;
            }
        }, err.getMessage()));
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        connectionEndpoints.add(this);
        var id = session.getId();
        try {
            var user = new User(new UserConfig(id, username));
            users.put(id, user);
        } catch(InvalidDataException err) {
            close(err);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        connectionEndpoints.remove(this);
    }

    public static void broadcast(MessageType msg) {
        connectionEndpoints.forEach(connectionEndpoint -> {
            synchronized (connectionEndpoint) {
                try {
                    connectionEndpoint.session.getBasicRemote().sendObject(msg);
                } catch (IOException | EncodeException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
