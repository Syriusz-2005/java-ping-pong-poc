package Client;

import Physics.PhysicsScene;

public class SceneManager {
    private int simulationStepsPerSecond;
    public final PhysicsScene scene = new PhysicsScene(PhysicsScene.DEFAULT_SCENE_CONFIG);

    public void setSimulationStepsPerSecond(int simulationStepsPerSecond) {
        this.simulationStepsPerSecond = simulationStepsPerSecond;
    }
    public int getSimulationStepsPerSecond() {
        return simulationStepsPerSecond;
    }
}
