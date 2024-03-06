package Physics;

import Vector.MutFVec2;
import Vector.MutFVector3;

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

    private Rectangle getIntersection(Rectangle r1, Rectangle r2) {
        var startPos1 = new MutFVec2(r1.pos.subtract(new MutFVec2(r1.width / 2, r1.height / 2)));
        var startPos2 = new MutFVec2(r2.pos.subtract(new MutFVec2(r2.width / 2, r2.height / 2)));


        float x1 = Math.max(startPos1.getX(), startPos2.getX());
        float y1 = Math.max(startPos1.getY(), startPos2.getY());

        float x2 = Math.min(startPos1.getX() + r1.width, startPos2.getX() + r2.width);
        float y2 = Math.min(startPos1.getY() + r1.height, startPos2.getY() + r2.height);

        if (x2 - x1 > 0 && y2 - y1 > 0) {
            var collision = new Rectangle(x2 - x1, y2 - y1);
            collision.pos.setX(x1).setY(y1);
            return collision;
        }

        return null;
    }

    /**
     * Apply all forces that are result of a AABB collision
     * @param r1
     * @param r2
     * @param i
     */
    private void calculateCollisionResult(Rectangle r1, Rectangle r2, Rectangle i) {
        var newPos1 = r1.pos.cloneVec();
        var newPos2 = r2.pos.cloneVec();
        float r1Commitment = r2.isImmovable ? 1 : .5f + (r2.mass / r1.mass);
        if (i.width < i.height) {
        }
    }

    /**
     * Performs collision detection using naive approach. Suitable for the situation
     * @param object
     */
    private void collisionDetect(Rectangle object) {
        if (object.canCollide || object.isImmovable) return;
        for (var pairedObject : objects) {
            if (pairedObject.canCollide) {
                float distance = object.pos.distanceTo(pairedObject.pos);
                var intersection = getIntersection(object, (Rectangle) pairedObject);
                if (intersection != null) {
                    calculateCollisionResult(object, (Rectangle) pairedObject, intersection);
                }
            }
        }
    }

    public void step(float stepLength) {
        for (var object : objects) {
            object.step(stepLength, config);
        }
    }

    public void step() {
        step(1);
    }
}
