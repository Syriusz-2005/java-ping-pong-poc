package Physics;

import Vector.MutFVec2;

public class Rectangle extends PhysicalObject {
    public float width;
    public float height;
    public Rectangle(float width, float height) {
        super();
        this.width = width;
        this.height = height;
    }

    public MutFVec2 getCornerPos() {
        return new MutFVec2(pos.subtract(new MutFVec2(width / 2, height / 2)));
    }

    public void applyCollisionForces(MutFVec2 newVelocity) {
        if (isImmovable || !canCollide) return;

        this.velocity = newVelocity;
    }
}
