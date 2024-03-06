package Server;

import Utils.CommandInterpreter;
import Utils.LogLevel;
import Utils.Logger;
import org.glassfish.tyrus.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logger.print("Starting the ping-pong server process...", LogLevel.REDUCED);
        Server server = new Server("localhost",
                8080,
                "/",
                null,
                ConnectionEndpoint.class);

        Lobby serverLobby = new Lobby(new LobbyConfig(10, 5));
        serverLobby.open();
        ConnectionEndpoint.initConnectionEndpoint(serverLobby);
        try {
            server.start();
            Logger.printOk("The server started and can process requests!");
            var interpreter = new CommandInterpreter("Enter command:");
            interpreter.listen((arguments) -> {
                switch (arguments[0]) {
                    case "exit" -> {
                        Logger.printOk("Stopping the server...");
                        return false;
                    }
                    case "removeAllGames" -> {
                        Logger.print("Removing all games...", LogLevel.REDUCED);
                        int gamesCount = serverLobby.closeAllGames();
                        Logger.print("Removed " + gamesCount + " games", LogLevel.REDUCED);
                    }
                }
                return true;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
