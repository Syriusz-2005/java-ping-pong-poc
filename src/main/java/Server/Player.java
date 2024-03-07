package Server;

import Message.MessageType;

/**
 * Player is a User that is playing the game.
 */
public class Player {
    private final User user;
    private final Game game;

    public Player(User user, Game game) {
        this.user = user;
        this.game = game;
    }

    public void dispose() {
        user.setPlayer(null);
    }

    public Game getGame() {
        return game;
    }

    public User getUser() {
        return user;
    }

    public void postMessage(MessageType msg) {
        user.postMessage(msg);
    }
}
