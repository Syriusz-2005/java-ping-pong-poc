package Renderer;
import Physics.PhysicsScene;
import Physics.Rectangle;
import Server.GameLoop;
import Vector.MutFVec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class GameSceneRenderer extends Thread {
    private long window;
    GLFWErrorCallback errorCallback;
    private int height = 600;
    private int width = height * 2;
    private final PhysicsScene scene;


    public GameSceneRenderer(PhysicsScene scene) {
        super();
        this.scene = scene;
    }

    private void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        glfwInit();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        window = glfwCreateWindow(width, height, "Ping pong game", NULL, NULL);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        IntBuffer framebufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
        width = framebufferSize.get(0);
        height = framebufferSize.get(1);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        glfwShowWindow(window);
    }


    private void render(ArrayList<Rectangle> objects) {

        for (var object : objects) {
            switch (object.getName()) {
                case "player0" -> glColor3f(1f, 1f, 0f);
                case "ceiling" -> glColor3f(0f, 0f, 1f);
                default -> glColor3f(1f, 1f, 1f);
            }

            if (object.mesh == null) {
                float[] vertices = new float[12];
                int[] indices = {0, 1, 2, 3, 4, 5, 6};
                object.mesh = MeshLoader.createMesh(vertices, indices, object);
            }

            var mesh = object.mesh;

            mesh.updateVertices();

            GL30.glBindVertexArray(mesh.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT,0);
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 1f);
        glLineWidth(2.8f);
        while (!glfwWindowShouldClose(window)) {
            var objects = scene.getObjects();

            glViewport(0, 0, width, height);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            var before = System.currentTimeMillis();
            render(objects);
            var now = System.currentTimeMillis();
            var delta = now - before;
//            System.out.println("Frame time: " + delta + "ms");

            glfwSwapBuffers(window);
            glfwPollEvents();

        }
    }

    @Override
    public void run() {
        try {
            init();
            loop();
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
}
