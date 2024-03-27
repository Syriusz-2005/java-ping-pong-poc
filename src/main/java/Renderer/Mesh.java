package Renderer;

import Physics.Rectangle;
import Server.GameLoop;
import Vector.MutFVec2;
import org.lwjgl.opengl.GL15;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class Mesh {

    private final int vao;
    private final int verticesCount;
    private final float[] verticesValues;
    private final Rectangle o;
    private FloatBuffer positionBuffer;
    public final int positionVbo;


    public Mesh(int vao, int vertex, float[] verticesValues, Rectangle o, FloatBuffer positionBuffer, int positionVbo) {
        this.vao = vao;
        this.verticesCount = vertex;
        this.verticesValues = verticesValues;
        this.o = o;
        this.positionBuffer = positionBuffer;
        this.positionVbo = positionVbo;
    }

    public void updateVertices() {
        var newValues = Mesh.updateVertices(o, verticesValues);
        var newBuffer = MeshLoader.createFloatBuffer(newValues);
//        positionBuffer.clear();
//        positionBuffer.put(0, newValues);
//        positionBuffer.flip();
        this.positionBuffer = newBuffer;
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.positionVbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, newBuffer);
    }

    public static float[] updateVertices(Rectangle o, float[] verticesValues) {
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

        for (int i = 0; i < vertices.length; i++) {
            verticesValues[i] = vertices[i];
        }

        return verticesValues;
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