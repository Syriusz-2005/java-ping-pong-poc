package Physics;

import Vector.MutFVector3;

import java.util.UUID;

public class PhysicalObject {
    public final String uuid = UUID.randomUUID().toString();

    protected MutFVector3 pos = new MutFVector3();
    protected MutFVector3 velocity = new MutFVector3();
    protected float mass = 1;

    public boolean canCollide = true;
    public boolean isImmovable = false;
    public boolean overridesAirFriction = false;
    public float airFrictionOverride;


    public void setPos(MutFVector3 pos) {
        this.pos = pos;
    }

    public void step(float stepSize, PhysicsSceneConfig config) {
        float friction = overridesAirFriction  ? airFrictionOverride : config.globalAirFriction();
        velocity.subtractLength(friction * stepSize);
        pos.add(velocity.cloneVec().multiplyScalar(stepSize));
    }
}
