package Renderer;
import Client.SceneManager;
import Physics.PhysicsScene;
import Physics.Rectangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private GLFWKeyCallback onKey;
    private final SceneManager sceneManager;
    public volatile AtomicBoolean shouldTerminate = new AtomicBoolean(false);


    public GameSceneRenderer(PhysicsScene scene, GLFWKeyCallback onKey, SceneManager manager) {
        super();
        this.scene = scene;
        this.onKey = onKey;
        this.sceneManager = manager;
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
        glfwSwapInterval(1);
    }

    private void render(ArrayList<Rectangle> objects) {
        sceneManager.autoPilot.step();
        for (var object : objects) {
            switch (object.getName()) {
                case "player0" -> glColor3f(1f, 1f, 0f);
                case "ceiling" -> glColor3f(0f, 0f, 1f);
                default -> glColor3f(1f, 1f, 1f);
            }

            if (object.mesh == null) {
                object.mesh = MeshLoader.createMesh(object);
            }

            var mesh = object.mesh;

            GL30.glBindVertexArray(mesh.getVaoID());

            mesh.updateVertices();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.positionVbo);
            GL20.glEnableVertexAttribArray(0);

            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT,0);

            GL20.glDisableVertexAttribArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            GL30.glBindVertexArray(0);
        }
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 1f);
        glLineWidth(2.8f);
        while (!glfwWindowShouldClose(window)) {
            var before = System.currentTimeMillis();
            var objects = scene.getObjects();

            glViewport(0, 0, width, height);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            

            render(objects);
//            System.out.println("Frame time: " + delta + "ms");

            glfwSwapBuffers(window);
            glfwPollEvents();
            var now = System.currentTimeMillis();
            var delta = now - before;
            var stepsPerSec = (float) sceneManager.getSimulationStepsPerSecond();
            var defaultSleepTime = 1000f / stepsPerSec;
            scene.step((100f / stepsPerSec) * (delta / defaultSleepTime));
        }
    }

    public void hideWindow() {
        glfwHideWindow(window);
    }

    public void showWindow() {
        glfwShowWindow(window);
    }

    @Override
    public void run() {
        try {
            init();
            glfwSetKeyCallback(window, onKey);
            loop();
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
}
