package Renderer;

import Client.ClientManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinGameListener implements ActionListener {
    private final ClientManager manager;
    private final JTextField gameCodeField;

    public JoinGameListener(ClientManager manager, JTextField gameCodeField) {
        this.manager = manager;
        this.gameCodeField = gameCodeField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        manager.joinGame(gameCodeField.getText());
    }
}
