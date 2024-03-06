package Server;

import Utils.Logger;
import org.glassfish.tyrus.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logger.print("Starting the ping-pong server process...");
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
            var scanner = new Scanner(System.in);
            Logger.print("Press enter to stop the server");
            scanner.nextLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
