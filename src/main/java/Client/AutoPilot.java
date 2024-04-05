package Client;

public class AutoPilot {
    private SceneManager sceneManager;
    public boolean isEnabled = false;

    public AutoPilot(SceneManager manager) {
        this.sceneManager = manager;
    }

    public void step() {
        if (!isEnabled) return;

    }
}
