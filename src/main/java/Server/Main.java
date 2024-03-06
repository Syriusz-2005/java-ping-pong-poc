package Server;

import Utils.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.printOk("Starting the ping-pong server process...");
        Lobby serverLobby = new Lobby(new LobbyConfig(10));
    }
}
