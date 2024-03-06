package Server;


import Utils.RandomCode;

import java.util.ArrayList;

public class Lobby {
    private LobbyConfig config;
    private final ArrayList<Game> games = new ArrayList<>();

    public Lobby(LobbyConfig config) {
        this.config = config;
    }

    private void createGame(User creator) throws Exception {
        if (games.size() >= config.maxGames()) {
            throw new Exception("Cannot create more games. Limit reached.");
        }
        String code = RandomCode.generateRandomCode(4, (result) -> games.stream().anyMatch((g) -> g.gameCode.equals(result)));
        var newGame = new Game(code);
        var player = new Player(creator, newGame);
        creator.setPlayer(player);
        try {
            newGame.addPlayer(player);
        } catch (Exception e) { // Not happening
            throw new RuntimeException(e);
        }
        games.add(newGame);
    }

    private boolean joinGame(User user, String gameCode) {
        return games
            .stream()
            .filter((g) -> g.gameCode.equals(gameCode) && g.getState() == GameState.WAITING_IN_LOBBY)
            .findFirst()
            .map((g) -> {
                Player p = new Player(user, g);
                user.setPlayer(p);
                try {
                    g.addPlayer(p);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return g;
            }).isPresent();
    }

    public void open() {
        ConnectionEndpoint.setAcceptsConnections(true);
    }

    public void close() {
        ConnectionEndpoint.setAcceptsConnections(false);
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}
