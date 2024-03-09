package Client;

import Message.CommandType;
import Message.MessageType;
import Utils.CommandInterpreter;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            var client = new WebsocketClient();
            String username = "_Syriusz_";
            var uri = new URI("ws://localhost:8080/connect/" + username);
            Session session = ContainerProvider.getWebSocketContainer().connectToServer(client, uri);

            var clientManager = new ClientManager(session);
            client.setManager(clientManager);
            var interpreter = new CommandInterpreter("");

            var connectionMessage = new MessageType();
            connectionMessage.setCommand(CommandType.TEST_CONNECTION);
            session.getBasicRemote().sendObject(connectionMessage);

            interpreter.listen((arguments) -> {
                var cmd = arguments[0];
                switch (cmd) {
                    case "create" -> {
                        clientManager.createGame();
                    }
                    case "join" -> {
                        clientManager.joinGame(arguments[1]);
                    }
                    case "exit" -> {
                        try {
                            session.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }
                return true;
            });
        } catch (URISyntaxException | DeploymentException | IOException | EncodeException e) {
            throw new RuntimeException(e);
        }

    }
}
