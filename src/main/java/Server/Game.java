package Server;

import Message.CommandType;
import Message.MessageType;

/**
 * A class that abstracts the Played game. It is a state machine that determines in what stage the game is.
 */
public class Game {
    private GameState state = GameState.WAITING_IN_LOBBY;
    public boolean isVisible = false;
    public String gameCode;

    private Player player0;
    private Player player1;

    public Game(String gameCode) {
        this.gameCode = gameCode;
    }
    private void broadcast(MessageType msg) {
        if (player0 != null) {
            player0.postMessage(msg);
        }
        if (player1 != null) {
            player1.postMessage(msg);
        }
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
        var msg = new MessageType().setCommand(CommandType.GAME_STATE_UPDATE);
        msg.data.gameStateUpdate.newState = state;
        broadcast(msg);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    /**
     *
     * @param p
     * @return boolean indicating if the lobby should dipose the game
     */
    public boolean removePlayer(Player p) {
        if (p == player0) {
            dispose();
            return true;
        } else {
            player1 = null;
            var msg = new MessageType().setCommand(CommandType.GAME_PLAYER_DISCONNECTED);
            broadcast(msg);
            setState(GameState.ENDED);
            return false;
        }
    }

    /**
     * Disposes the game state and informs players that the game is being closed
     */
    public void dispose() {
        if (player0 != null) {
            player0.dispose();
        }
        if (player1 != null) {
            player1.dispose();
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "state=" + state +
                ", isVisible=" + isVisible +
                ", gameCode='" + gameCode + '\'' +
                ", player0=" + player0 +
                ", player1=" + player1 +
                '}';
    }
}
