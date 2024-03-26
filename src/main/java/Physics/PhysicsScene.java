package Physics;

import Vector.MutFVec2;
import Vector.MutFVector3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Basic AABB Collision detection and physics engine
 * Scene represents a place in which PhysicalObjects interfere with themselves
 */
public class PhysicsScene {
    public static PhysicsSceneConfig DEFAULT_SCENE_CONFIG = new PhysicsSceneConfig(0);

    private final PhysicsSceneConfig config;
    private final ArrayList<Rectangle> objects = new ArrayList<>();

    public PhysicsScene(PhysicsSceneConfig config) {
        this.config = config;
    }

    public void add(Rectangle object) {
        objects.add(object);
    }

    public void remove(Rectangle object) {
        objects.remove(object);
    }

    public void removeAll() {
        objects.clear();
    }

    public void applyCorrection(Rectangle[] arr) {
        synchronized (objects) {
            outer: for (var rec : arr) {
                for (var object : objects) {
                    if (rec.uuid.equals(object.uuid)) {
                        object.updateFromRectangle(rec);
                        continue outer;
                    }
                }
                // No matching object found: create new
                objects.add(rec);
            }
        }
    }

    public ArrayList<Rectangle> getObjects() {
        return this.objects;
    }

    /**
     * Finds the intersection box if it exists
     * @param r1
     * @param r2
     * @return Rectangle or null
     */
    private Rectangle getIntersection(Rectangle r1, Rectangle r2) {
        var topLeft1 = r1.getCornerPos();
        var topLeft2 = r2.getCornerPos();

        var bottomRight1 = r1.getBottomRight();
        var bottomRight2 = r2.getBottomRight();

        float x1 = Math.max(topLeft1.getX(), topLeft2.getX());
        float y1 = Math.min(topLeft1.getY(), topLeft2.getY());

        float x2 = Math.min(bottomRight1.getX(), bottomRight2.getX());
        float y2 = Math.max(bottomRight1.getY(), bottomRight2.getY());

        if (x2 - x1 > 0 && y2 - y1 < 0) {
            var collision = new Rectangle(x2 - x1, y1 - y2, null);
            collision.pos.setX(x1).setY(y1);
            return collision;
        }

        return null;
    }

    /**
     * Apply all forces that are a result of a AABB collision
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
            var rvx = (r1.velocity.getX() + r2.velocity.getX());
            float direction = iCorner.getX() < r1.pos.getX() ? 1 : -1;
            newPos1.addX((float) i.width * direction * r1Commitment);
            newPos2.addX((float) i.width * direction * r2Commitment);
            r1.velocity.setX(-rvx * r1Commitment);
            r2.velocity.setX(rvx * r2Commitment);
        } else {
            var rvy = (r1.velocity.getY() + r2.velocity.getY());
            float direction = iCorner.getY() < r1.pos.getY() ? 1 : -1;
            newPos1.addY((float) i.height * r1Commitment * direction);
            newPos2.addY((float) i.height * r2Commitment * direction);
            r1.velocity.setY(-rvy * r1Commitment);
            r2.velocity.setY(rvy * r2Commitment);
        }
        r1.pos.set(newPos1);
        r2.pos.set(newPos2);
    }

    /**
     * Performs collision detection using naive approach. Suitable for scenes with low amount of objects
     * TODO: Implement Continues collision detection for precise simulation
     * @param object
     */
    private void collisionDetect(Rectangle object) {
        if (!object.canCollide || object.isImmovable) return;
        for (var pairedObject : objects) {
            if (pairedObject.canCollide && object != pairedObject) {
                var intersection = getIntersection(object, pairedObject);
                if (intersection != null) {
//                    System.out.println("Collision detected!");
//                    System.out.println(object);
//                    System.out.println(pairedObject);
//                    System.out.println("\n");
                    calculateCollisionResult(object, pairedObject, intersection);
                }
            }
        }
    }

    public void step(float stepLength) {
        for (var object : objects) {
            object.step(stepLength, config);
            collisionDetect(object);
        }
    }

    public Rectangle findObject(String uuid) {
        for (var object : objects) {
            if (object.uuid.equals(uuid)) {
                return object;
            }
        }
        return null;
    }

    public void step() {
        step(1);
    }
}
