package Renderer;

import Physics.Rectangle;
import Server.GameLoop;
import Vector.MutFVec2;

import java.nio.FloatBuffer;

public class Mesh {

    private final int vao;
    private final int verticesCount;
    private final float[] verticesValues;
    private final Rectangle o;
    private final FloatBuffer positionBuffer;
    private final int positionVbo;


    public Mesh(int vao, int vertex, float[] verticesValues, Rectangle o, FloatBuffer positionBuffer, int positionVbo) {
        this.vao = vao;
        this.verticesCount = vertex;
        this.verticesValues = verticesValues;
        this.o = o;
        this.positionBuffer = positionBuffer;
        this.positionVbo = positionVbo;
    }

    public void updateVertices() {
        Mesh.updateVertices(o, verticesValues);
        positionBuffer.put(0, verticesValues);
        positionBuffer.rewind();

    }

    public static void updateVertices(Rectangle o, float[] verticesValues) {
        var viewport = new MutFVec2(GameLoop.SCENE_WIDTH, GameLoop.SCENE_HEIGHT);

        var topLeft = convertVec(o.getCornerPos().divide(viewport));
        var topRight = convertVec(o.getCornerPos().addX(o.width).divide(viewport));
        var bottomLeft = convertVec(o.getCornerPos().addY(-o.height).divide(viewport));
        var bottomRight = convertVec(o.getCornerPos().addY(-o.height).addX(o.width).divide(viewport));

        float[] vertices = {
                topLeft.getX(), topLeft.getY(),
                topRight.getX(), topRight.getY(),
                bottomLeft.getX(), bottomLeft.getY(),

                topRight.getX(), topRight.getY(),
                bottomLeft.getX(), bottomLeft.getY(),
                bottomRight.getX(), bottomRight.getY(),
        };

        System.arraycopy(vertices, 0, verticesValues, 0, vertices.length);
    }

    private static MutFVec2 convertVec(MutFVec2 vec) {
        return vec.multiplyScalar(2f).subtractScalar(1f);
    }

    public int getVaoID() {
        return vao;
    }

    public int getVertexCount() {
        return verticesCount;
    }
}