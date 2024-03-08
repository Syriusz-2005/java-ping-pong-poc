package Server;


import Message.CommandType;
import Message.MessageType;
import Utils.LogLevel;
import Utils.Logger;
import Utils.RandomCode;

import java.util.ArrayList;

public class Lobby {
    private LobbyConfig config;
    private ArrayList<Game> games = new ArrayList<>();

    public Lobby(LobbyConfig config) {
        this.config = config;
    }

    public void createGame(User creator) throws Exception {
        if (games.size() >= config.maxGames()) {
            throw new GameNotCreatedException("Cannot create more games. Limit reached.");
        }
        if (creator.getPlayer() != null) {
            throw new GameNotCreatedException("Cannot create game. User is in other game");
        }
        String code = RandomCode.generateRandomCode(4, (result) -> games.stream().anyMatch((g) -> g.gameCode.equals(result)));
        var newGame = new Game(code);
        var player = new Player(creator, newGame);
        creator.setPlayer(player);
        try {
            newGame.addPlayer(player);
            games.add(newGame);
            Logger.print("New game created, code: " + newGame.gameCode, LogLevel.SPECIFIC);
        } catch (Exception e) { // Not happening
            throw new RuntimeException(e);
        }
    }

    public boolean joinGame(User user, String gameCode) {
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

    public Game findGame(String gameCode) {
        for (var game : games) {
            if (game.gameCode.equals(gameCode)) {
                return game;
            }
        }
        return null;
    }

    public int closeAllGames() {
        int size = games.size();
        for (var game : games) {
            game.dispose();
        }
        games = new ArrayList<>();
        return size;
    }

    private void disposeGame(Game g) {
        games.remove(g);
    }

    public void removeClosedConnection(User user) {
        var player = user.getPlayer();
        if (player != null) {
            var game = player.getGame();
            boolean shouldDispose = game.removePlayer(player);
            if (shouldDispose) {
                disposeGame(game);
            };
        }
    }
}
