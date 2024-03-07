package Server;

import Message.CommandType;
import Message.MessageType;
import org.eclipse.jetty.util.Atomics;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that abstracts the Played game. It is a state machine that determines in what stage the game is.
 */
public class Game {
    private volatile GameState state = GameState.WAITING_IN_LOBBY;
    public boolean isVisible = false;
    public String gameCode;

    public AtomicBoolean isDisposed = new AtomicBoolean(false);

    private Player player0;
    private Player player1;

    private final GameLoop gameLoop = new GameLoop(this, new GameLoopConfig(40, 2));

    public Game(String gameCode) {
        this.gameCode = gameCode;
        gameLoop.start();
    }
    private void broadcast(MessageType msg) {
        if (player0 != null) {
            player0.postMessage(msg);
        }
        if (player1 != null) {
            player1.postMessage(msg);
        }
    }

    public void broadcastMetadata() {
        var msg1 = new MessageType().setCommand(CommandType.GAME_METADATA_UPDATE);
        msg1.data.gameMetadataUpdate.yourUUID = player0.sceneObjectUUID;
        player0.postMessage(msg1);

        var msg2 = new MessageType().setCommand(CommandType.GAME_METADATA_UPDATE);
        msg2.data.gameMetadataUpdate.yourUUID = player1.sceneObjectUUID;
        player1.postMessage(msg2);
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
        if (state == GameState.PREPARING) {
            gameLoop.initializeScene();
        }
        var msg = new MessageType().setCommand(CommandType.GAME_STATE_UPDATE);
        msg.data.gameStateUpdate.newState = state;
        broadcast(msg);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }


    /**
     * Automatically calls game.dispose() if necessary
     * @param p
     * @return boolean indicating if the lobby should dispose the game
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
     * This method must be invoked before closing the game to free up resources!
     *
     */
    public void dispose() {
        isDisposed.set(true);
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
