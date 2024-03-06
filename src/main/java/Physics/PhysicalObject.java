package Physics;

import Vector.MutFVec2;
import Vector.MutFVector3;

import java.util.UUID;

public class PhysicalObject {
    public final String uuid = UUID.randomUUID().toString();

    protected MutFVec2 pos = new MutFVec2();
    protected MutFVec2 velocity = new MutFVec2();
    /**
     * Mass must be normalised
     * 0 means no mass at all
     * 1 means a large mass
     */
    protected float mass = .5f;

    public PhysicalObject setMass(float newMass) throws OutOfBoundsException {
        if (newMass <= 0 || newMass > 1) {
            throw new OutOfBoundsException();
        }
        mass = newMass;
        return this;
    }

    public boolean canCollide = true;
    public boolean isImmovable = false;
    public boolean overridesAirFriction = false;
    public float airFrictionOverride;


    public void setPos(MutFVec2 pos) {
        this.pos = pos;
    }

    public void step(float stepSize, PhysicsSceneConfig config) {
        float friction = overridesAirFriction  ? airFrictionOverride : config.globalAirFriction();
        velocity.subtractLength(friction * stepSize);
        pos.add(velocity.cloneVec().multiplyScalar(stepSize));
    }


}
