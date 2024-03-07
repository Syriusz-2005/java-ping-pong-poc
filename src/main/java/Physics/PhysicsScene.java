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

    public void removeAll() {
        objects.clear();
    }

    private Rectangle getIntersection(Rectangle r1, Rectangle r2) {
        var startPos1 = r1.getCornerPos();
        var startPos2 = r2.getCornerPos();


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
        float r1Commitment = r2.isImmovable ? 1 : (r1.mass < r2.mass ? 1 - (r1.mass / r2.mass) : r2.mass - r1.mass);
        float r2Commitment = 1 - r1Commitment;
        MutFVec2 iCorner = i.getCornerPos();
        if (i.width < i.height) {
            var rvx = ((r1.pos.getX() + r2.pos.getX()) / 2);
            float direction = iCorner.getX() < r1.pos.getX() ? 1 : -1;
            newPos1.addX((float) i.width * direction * r1Commitment);
            newPos2.addX((float) i.width * (-direction) * r2Commitment);
            r1.velocity.setX(rvx * r1Commitment);
            r2.velocity.setX(-rvx * r2Commitment);
        } else {
            var rvy = ((r1.pos.getY() + r2.pos.getY()) / 2);
            float direction = iCorner.getY() < r1.pos.getY() ? 1 : -1;
            newPos1.addY((float) i.height * direction * r1Commitment);
            newPos2.addY((float) i.height * (-direction) * r2Commitment);
            r1.velocity.setY(rvy * r1Commitment);
            r2.velocity.setY(-rvy * r2Commitment);
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
