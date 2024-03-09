package Physics;

import Vector.MutFVec2;
import Vector.MutFVector3;

import java.util.UUID;

public class PhysicalObject {
    public String uuid;

    protected MutFVec2 pos = new MutFVec2();
    protected MutFVec2 velocity = new MutFVec2();
    /**
     * Mass of an object
     */
    protected float mass = .5f;

    public PhysicalObject() {
        this.uuid = UUID.randomUUID().toString();
    }

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
        float friction = overridesAirFriction ? airFrictionOverride : config.globalAirFriction();
        velocity.subtractLength(friction * stepSize);
        pos.add(velocity.cloneVec().multiplyScalar(stepSize));
    }

    @Override
    public String toString() {
        return "PhysicalObject{" +
                "uuid='" + uuid + '\'' +
                ", pos=" + pos +
                ", velocity=" + velocity +
                ", mass=" + mass +
                ", canCollide=" + canCollide +
                ", isImmovable=" + isImmovable +
                ", overridesAirFriction=" + overridesAirFriction +
                ", airFrictionOverride=" + airFrictionOverride +
                '}';
    }
}
