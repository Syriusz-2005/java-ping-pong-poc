package Renderer;
import Client.ClientManager;

import javax.swing.*;
import java.awt.*;

public class WindowRenderer extends JFrame {
    private final ClientManager manager;
    private JPanel lobbyFrame;

    public WindowRenderer(ClientManager manager) {
        super("Scene renderer");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayJoinMenu();
        setVisible(true);
        setLayout(new FlowLayout());
        float size = 600;
        setSize((int) size * 2, (int) size);
    }

    public void displayLobbyMenu() {
        String gameCode = manager.getGameCode();
        lobbyFrame.setVisible(false);
        System.out.println("Changing to lobby menu");
        var gameCodePanel = new JPanel();

        var label = new JLabel("Your game code: ");
        var codeInput = new JTextField();
        codeInput.setSize(100, 30);
        codeInput.setText(gameCode);
        codeInput.setEnabled(false);
        gameCodePanel.add(label);
        gameCodePanel.add(codeInput);

        var label2 = new JLabel("Waiting for an oponent to start...");

        var main = new JPanel();
        var mainLayout = new GridLayout(2, 1);
        mainLayout.setVgap(10);
        main.setLayout(mainLayout);
        main.add(gameCodePanel);
        main.add(label2);

        add(main);

        revalidate();
        System.out.println("Menu changed");
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
        createGameButton.addActionListener(new CreateGameListener(manager));

        JPanel parentPanel = new JPanel();
        var parentLayout = new BorderLayout();
        parentLayout.setVgap(10);
        parentPanel.setLayout(parentLayout);
        var label = new JLabel("Or");
        parentPanel.add(joinGamePanel, BorderLayout.NORTH);
        parentPanel.add(label, BorderLayout.CENTER);
        parentPanel.add(createGameButton, BorderLayout.SOUTH);

        lobbyFrame = parentPanel;
        add(parentPanel);
    }
}
