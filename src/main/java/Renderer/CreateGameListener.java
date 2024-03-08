package Renderer;

import Client.ClientManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateGameListener implements ActionListener {
    private final ClientManager manager;
    public CreateGameListener(ClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.createGame();
            }
        });
    }
}
