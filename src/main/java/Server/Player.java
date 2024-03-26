package Server;

import Message.MessageType;
import Vector.MutFVec2;

/**
 * Player is a User that is playing the game.
 */
public class Player {
    private final User user;
    private final Game game;
    public volatile String sceneObjectUUID;

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

    public void setMovement(int direction) {
        System.out.println("Setting movement");
        var scene = game.getGameLoop().getScene();
        var object = scene.findObject(sceneObjectUUID);
        //funny line for potential cheats
        var movement = new MutFVec2(0, 1f).multiplyScalar(direction);
        object.setVelocity(movement);
        System.out.println(movement);
        System.out.println(object.getVelocity());

    }
}
