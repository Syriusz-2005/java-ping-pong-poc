package Server;

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
}
