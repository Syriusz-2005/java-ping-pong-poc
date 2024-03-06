package Physics;

import java.util.ArrayList;

/**
 * Basic AABB Collision detection and physics engine
 * Scene represents a place in which PhysicalObjects interfere with themselves
 */
public class PhysicsScene {
    private final PhysicsSceneConfig config;
    private ArrayList<PhysicalObject> objects;

    public PhysicsScene(PhysicsSceneConfig config) {
        this.config = config;
    }

    public void add(PhysicalObject object) {
        objects.add(object);
    }

    public void remove(PhysicalObject object) {
        objects.remove(object);
    }

    public void step(float stepLength) {

    }

    public void step() {
        step(1);
    }
}
