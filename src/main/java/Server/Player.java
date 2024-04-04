package Server;

import Message.CommandType;
import Message.MessageType;
import Vector.MutFVec2;

/**
 * Player is a User that is playing the game.
 */
public class Player {
    private final User user;
    private final Game game;
    public volatile String sceneObjectUUID;
    private boolean isWinner;

    public Player(User user, Game game) {
        this.user = user;
        this.game = game;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
        postEndSummaryMessage();
    }

    public void postEndSummaryMessage() {
        var msg = new MessageType().setCommand(CommandType.GAME_END_SUMMARY);
        msg.data.gameEndSummary.isWin = isWinner;
        postMessage(msg);
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

    public void setMovement(int direction) {
        var scene = game.getGameLoop().getScene();
        var object = scene.findObject(sceneObjectUUID);
        //funny line for potential cheats (Could have made it cheat-proof, but nobody cares)
        var movement = new MutFVec2(0, 1f).multiplyScalar(direction);
        object.setVelocity(movement);
    }
}
