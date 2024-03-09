package Physics;

import Vector.MutFVec2;

public class Rectangle extends PhysicalObject {

    public float width;
    public float height;
    public Rectangle(float width, float height, String name) {
        super(name);
        this.width = width;
        this.height = height;
    }

    public MutFVec2 getCornerPos() {
        return new MutFVec2(pos.cloneVec().subtract(new MutFVec2(width / 2, -height / 2)));
    }

    public void applyCollisionForces(MutFVec2 newVelocity) {
        if (isImmovable || !canCollide) return;

        this.velocity = newVelocity;
    }

    public void updateFromRectangle(Rectangle rec) {
        width = rec.width;
        height = rec.height;
        pos = new MutFVec2(rec.pos.getX(), rec.pos.getY());
        velocity = new MutFVec2(rec.velocity.getX(), rec.velocity.getX());
        uuid = rec.uuid;
    }
}
