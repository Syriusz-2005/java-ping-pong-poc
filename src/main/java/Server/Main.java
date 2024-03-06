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
            var interpreter = new CommandInterpreter("");
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
                    case "inspect" -> {
                        String gameCode = arguments[1];
                        var game = serverLobby.findGame(gameCode);
                        if (game != null) {
                            Logger.printErr("This game does not exist!");
                            return true;
                        }
                        System.out.println(game);
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
