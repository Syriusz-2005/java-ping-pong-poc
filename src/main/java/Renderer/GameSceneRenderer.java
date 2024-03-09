package Renderer;
import Physics.PhysicsScene;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

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

        window = glfwCreateWindow(width, height, "Polygon Demo", NULL, NULL);

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

    private void render() {
        var objects = scene.getObjects();
        synchronized (objects) {
            System.out.print(" " + objects.size());

            glColor3f(1f, 1f, 1f);
            for (var object : objects) {
                var topLeft = object.getCornerPos();
                var topRight = object.getCornerPos().cloneVec().addX(object.width);
                var bottomLeft = object.getCornerPos().cloneVec().addY(object.height);
                var bottomRight = object.getCornerPos().cloneVec().addY(object.height).addX(object.width);

                glBegin(GL_LINE_STRIP);

                glVertex2f(topLeft.getX(), topLeft.getY());
                glVertex2f(topRight.getX(), topRight.getY());
                glVertex2f(bottomLeft.getX(), bottomLeft.getY());

                glVertex2f(topRight.getX(), topRight.getY());
                glVertex2f(bottomLeft.getX(), bottomLeft.getY());
                glVertex2f(bottomRight.getX(), bottomRight.getY());

                glEnd();
            }
        }
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 1f);
        glLineWidth(1.8f);

        while (!glfwWindowShouldClose(window)) {
            glViewport(0, 0, width, height);
            glClear(GL_COLOR_BUFFER_BIT);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, width, height, 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            render();

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
