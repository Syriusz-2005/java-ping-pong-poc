package Client;

import Message.CommandType;
import Message.MessageType;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {

        var client = new WebsocketClient();
        String username = "_Syriusz_";
        try {
            var uri = new URI("ws://localhost:8080/connect/" + username);
            Session session = ContainerProvider.getWebSocketContainer().connectToServer(client, uri);
            var connectionMessage = new MessageType();
            connectionMessage.setCommand(CommandType.TEST_CONNECTION);
            session.getBasicRemote().sendObject(connectionMessage);
        } catch (URISyntaxException | DeploymentException | IOException | EncodeException e) {
            throw new RuntimeException(e);
        }

    }
}
