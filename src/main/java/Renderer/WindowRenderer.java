package Renderer;
import javax.swing.*;
import java.awt.*;

public class WindowRenderer extends JFrame {
    public WindowRenderer() {
        super("Scene renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayJoinMenu();
        setVisible(true);
        setLayout(new FlowLayout());
        float size = 600;
        setSize((int) size * 2, (int) size);
    }

    public void displayJoinMenu() {
        var field = new JTextField();
        field.setSize(100, 30);
        var codeInputTextLabel = new JLabel("Game code:");
        var joinGameButton = new JButton("Join game");

        var joinGamePanel = new JPanel();
        joinGamePanel.setLayout(new GridLayout(3, 1, 10, 10));
        joinGamePanel.add(codeInputTextLabel);
        joinGamePanel.add(field);
        joinGamePanel.add(joinGameButton);

        var createGameButton = new JButton("Create new game");

        JPanel parentPanel = new JPanel();
        var parentLayout = new BorderLayout();
        parentLayout.setVgap(10);
        parentPanel.setLayout(parentLayout);
        var label = new JLabel("Or");
        label.setText("Or");
        parentPanel.add(joinGamePanel, BorderLayout.NORTH);
        parentPanel.add(label, BorderLayout.CENTER);
        parentPanel.add(createGameButton, BorderLayout.SOUTH);

        add(parentPanel);
    }
}
