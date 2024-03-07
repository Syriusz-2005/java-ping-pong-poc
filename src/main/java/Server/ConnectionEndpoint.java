package Server;

import Message.CommandType;
import Message.MessageDecoder;
import Message.MessageEncoder;
import Message.MessageType;
import Utils.LogLevel;
import Utils.Logger;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/connect/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
@WebSocket
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
            var user = new User(new UserConfig(id, username, session, lobby, this));
            users.put(id, user);
        } catch(InvalidDataException err) {
            close(err);
        }
    }

    public void postMessage(MessageType msg) {
        session.getAsyncRemote().sendObject(msg);
    }

    @OnMessage
    public void onMessage(MessageType message) throws Exception {
        Logger.print("Message received:", LogLevel.VERY_SPECIFIC);
        Logger.print(message.toString(), LogLevel.VERY_SPECIFIC);
        var user = users.get(session.getId());
        switch (message.getCommand()) {
            case TEST_CONNECTION -> {
                var pongMessage = new MessageType().setCommand(CommandType.TEST_CONNECTION);
                postMessage(pongMessage);
            }
            case CREATE_GAME -> {
                ConnectionEndpoint.lobby.createGame(user);
            }
            case GAME_JOIN_REQUEST -> {
                ConnectionEndpoint.lobby.joinGame(user, message.data.gameCode);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        var user = users.get(session.getId());
        lobby.removeClosedConnection(user);
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
