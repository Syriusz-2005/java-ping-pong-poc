package Renderer;
import Client.ClientManager;

import javax.swing.*;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

public class WindowRenderer extends JFrame {
    private final ClientManager manager;
    private JPanel lobbyFrame;
    private JPanel waitingFrameMenu;
    private JPanel gameEndMenu;

    public WindowRenderer(ClientManager manager) {
        super("Ping Pong lobby");
        this.manager = manager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayJoinMenu();
        setVisible(true);
        setLayout(new FlowLayout());
        float size = 600;
        setSize((int) size * 2, (int) size);

    }

    public void displayPreparingMenu() {
        System.out.println("Changing to preparing menu");
        waitingFrameMenu.setVisible(false);
    }

    public void displayWaitingMenu() {
        String gameCode = manager.getGameCode();
        lobbyFrame.setVisible(false);
        System.out.println("Changing to waiting menu");
        var gameCodePanel = new JPanel();

        var label = new JLabel("Your game code: " + gameCode);
        gameCodePanel.add(label);

        var label2 = new JLabel("Waiting for an oponent to start...");

        var main = new JPanel();
        var mainLayout = new GridLayout(2, 1);
        mainLayout.setVgap(10);
        main.setLayout(mainLayout);
        main.add(gameCodePanel);
        main.add(label2);

        add(main);
        waitingFrameMenu = main;

        revalidate();
    }

    public void displayGameEndMenu(boolean isWinner) {
        var parent = new JPanel();

        var label = new JLabel("Game ended!");

        var label2 = new JLabel(isWinner ? "You won!" : "You lost...");
        var returnToLobbyButton = new Button("Play again");
        returnToLobbyButton.addActionListener(new LeaveGameListener(manager, this));

        parent.add(label);
        parent.add(label2);
        parent.add(returnToLobbyButton);

        add(parent);
        gameEndMenu = parent;

        revalidate();
    }

    public void displayJoinMenu() {
        // Why can't I just make ui in html/css??
        if (gameEndMenu != null) {
            gameEndMenu.setVisible(false);
        }
        var codeField = new TextField("Enter game code:");
        var joinGameButton = new Button("Join game");
        joinGameButton.addActionListener(new JoinGameListener(manager, codeField.getField()));

        var joinGamePanel = new JPanel();
        var layout = new GridBagLayout();

        var constraints = new GridBagConstraints();

        joinGamePanel.setLayout(layout);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        joinGamePanel.add(codeField, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        joinGamePanel.add(joinGameButton, constraints);

        var createGameButton = new Button("Create new game");
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
