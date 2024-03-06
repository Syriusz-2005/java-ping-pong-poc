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
    private static boolean acceptsConnections = false;
    private Session session;
    private static final Set<ConnectionEndpoint> connectionEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, User> users = new HashMap<>();
    private static Lobby lobby;

    public static void initConnectionEndpoint(Lobby lobby) {
        ConnectionEndpoint.lobby = lobby;
    }


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
        if (!acceptsConnections) {
            session.close();
            return;
        }
        this.session = session;
        connectionEndpoints.add(this);
        var id = session.getId();
        try {
            var user = new User(new UserConfig(id, username, session, lobby));
            users.put(id, user);
        } catch(InvalidDataException err) {
            close(err);
        }
    }

    @OnMessage
    public void onMessage(MessageType message) {
        System.out.println("Message received");
        System.out.println(message);
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

    public static void setAcceptsConnections(boolean acceptsConnections) {
        ConnectionEndpoint.acceptsConnections = acceptsConnections;
    }
}
