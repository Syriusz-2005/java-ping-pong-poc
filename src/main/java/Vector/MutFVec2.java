package Vector;

import java.util.ArrayList;

public class MutFVec2 implements Vector<Float> {
    private float x;
    private float y;

    public MutFVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public MutFVec2(MutFVec2 vec) {
        x = vec.x;
        y = vec.y;
    }

    public MutFVec2() {
        x = 0;
        y = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public MutFVec2 setX(float val) {
        x = val;
        return this;
    }

    public MutFVec2 setY(float val) {
        y = val;
        return this;
    }

    public MutFVec2 set(MutFVec2 vec) {
        x = vec.x;
        y = vec.y;
        return this;
    }

    public MutFVec2 addX(float val) {
        x += val;
        return this;
    }

    public MutFVec2 addY(float val) {
        y += val;
        return this;
    }

    public MutFVec2 addScalar(float s) {
        x += s;
        y += s;

        return this;
    }

    public MutFVec2 add(MutFVec2 vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    public MutFVec2 subtractScalar(float s) {
        x -= s;
        y -= s;

        return this;
    }

    public MutFVec2 subtract(MutFVec2 vec) {
        x -= vec.x;
        y -= vec.y;

        return this;
    }

    public MutFVec2 multiplyScalar(float s) {
        x *= s;
        y *= s;
        return this;
    }

    public MutFVec2 mulX(float s) {
        x *= s;
        return this;
    }

    public MutFVec2 mulY(float s) {
        y *= s;
        return this;
    }

    public MutFVec2 multiply(MutFVec2 vec) {
        x *= vec.x;
        y *= vec.y;

        return this;
    }

    public MutFVec2 divide(MutFVec2 vec) {
        x /= vec.x;
        y /= vec.y;
        return this;
    }

    public MutFVec2 divideScalar(float f) {
        x /= f;
        y /= f;
        return this;
    }

    public MutFVec2 normalise() {
        float l = length();
        divideScalar(l);
        return this;
    }

    public MutFVec2 subtractLength(float value) {
        x = x > 0 ? x - value : x + value;
        y = y > 0 ? y - value : y + value;
        return this;
    }

    public float distanceTo(MutFVec2 vec) {
        MutFVec2 delta = subtract(vec);
        return (float) Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.y, 2));
    }

    public float length() {
        var pointZero = new MutFVec2();
        return distanceTo(pointZero);
    }

    public float dot(MutFVec2 vec) {
        return x * vec.x + y * vec.y;
    }

    public MutFVec2 invert() {
        x = -x;
        y = -y;
        return this;
    }

    public MutFVec2 clone(MutFVec2 vec) {
        x = vec.x;
        y = vec.y;
        return this;
    }

    @Override
    public MutFVec2 cloneVec() {
        return new MutFVec2(this);
    }

    @Override
    public ArrayList<Float> toList() {
        var list = new ArrayList<Float>();
        list.add(x);
        list.add(y);
        return list;
    }

    @Override
    public String toString() {
        return "MutFVec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
