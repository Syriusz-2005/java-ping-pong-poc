package Renderer;

import Client.ClientManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaveGameListener implements ActionListener {
    private final ClientManager manager;
    private final WindowRenderer renderer;

    public LeaveGameListener(ClientManager manager, WindowRenderer renderer) {
        this.manager = manager;
        this.renderer = renderer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            manager.connectionManager.leaveGame();
            renderer.displayJoinMenu();
        });
    }
}
