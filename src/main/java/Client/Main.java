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

        var client = new WebsocketClient();
        String username = "_Syriusz_";
        try {
            var uri = new URI("ws://localhost:8080/connect/" + username);
            Session session = ContainerProvider.getWebSocketContainer().connectToServer(client, uri);
            var connectionMessage = new MessageType();
            connectionMessage.setCommand(CommandType.TEST_CONNECTION);
            session.getBasicRemote().sendObject(connectionMessage);

            var clientManager = new ClientManager(session);
            var interpreter = new CommandInterpreter("");
            interpreter.listen((arguments) -> {
                var cmd = arguments[0];
                switch (cmd) {
                    case "createGame" -> {
                        var msg = new MessageType().setCommand(CommandType.CREATE_GAME);
                        clientManager.connectionManager.postMessage(msg);
                    }
                    case "join" -> {
                        var msg = new MessageType().setCommand(CommandType.GAME_JOIN_REQUEST);
                        msg.data.gameCode = arguments[1];
                        clientManager.connectionManager.postMessage(msg);
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
