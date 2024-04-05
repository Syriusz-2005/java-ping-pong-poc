package Client;

import Physics.PhysicsScene;
import Physics.Rectangle;

public class SceneManager {
    private int simulationStepsPerSecond;
    public final PhysicsScene scene = new PhysicsScene(PhysicsScene.DEFAULT_SCENE_CONFIG);
    private String playerUUID;

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setSimulationStepsPerSecond(int simulationStepsPerSecond) {
        this.simulationStepsPerSecond = simulationStepsPerSecond;
    }
    public int getSimulationStepsPerSecond() {
        return simulationStepsPerSecond;
    }

    public Rectangle getCurrentPlayer() {
        return scene.findObject(playerUUID);
    }

    public Rectangle getBall() {
        return scene.findObject((p) -> p.getName().equals("ball"));
    }
}
