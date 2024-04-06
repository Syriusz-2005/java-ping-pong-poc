package Client;

import Message.CommandType;
import Message.MessageType;

public class AutoPilot {
    private final SceneManager sceneManager;
    private final ConnectionManager connectionManager;
    public boolean isEnabled = false;
    private int i = 0;

    public AutoPilot(SceneManager manager, ConnectionManager connectionManager) {
        this.sceneManager = manager;
        this.connectionManager = connectionManager;
    }

    public void step() {
        if (!isEnabled) return;
        i++;
        if (i % 10 != 0) return;

        var ball = sceneManager.getBall();
        var palette = sceneManager.getPalette();

        if (ball == null || palette == null) return;

        var msg = new MessageType().setCommand(CommandType.KEY_STATE_UPDATE);
        if (palette.getPos().getY() > ball.getPos().getY()) {
            msg.data.keyState.paletteMovement = -1;
        } else {
            msg.data.keyState.paletteMovement = 1;
        }
        connectionManager.postMessage(msg);
    }
}
