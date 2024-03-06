package Server;

public class Game {
    private GameState state = GameState.WAITING_IN_LOBBY;
    public boolean isVisible = false;
    public String gameCode;

    private Player player0;
    private Player player1;

    public Game(String gameCode) {
        this.gameCode = gameCode;
    }

    public void addPlayer(Player p) throws Exception {
        if (state == GameState.WAITING_IN_LOBBY) {
            if (player0 == null) {
                player0 = p;
            } else {
                player1 = p;
                setState(GameState.PREPARING);
            }
            return;
        }
        throw new Exception("Cannot add players to the active game");
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void dispose() {
        if (player0 != null) {
            player0.dispose();
        }
        if (player1 != null) {
            player1.dispose();
        }
    }
}
