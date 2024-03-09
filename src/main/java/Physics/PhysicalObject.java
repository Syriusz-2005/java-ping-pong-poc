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
    private String name;

    public PhysicalObject(String name) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
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

    public MutFVec2 getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public void setVelocity(MutFVec2 velocity) {
        this.velocity = velocity;
    }

    public void step(float stepSize, PhysicsSceneConfig config) {
        float friction = overridesAirFriction ? airFrictionOverride : config.globalAirFriction();
        velocity.subtractLength(friction * stepSize);
        var posDelta = velocity.cloneVec().multiplyScalar(stepSize);
        pos.add(posDelta);
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
