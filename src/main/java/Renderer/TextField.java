package Renderer;

import javax.swing.*;
import java.awt.*;

public class TextField extends JPanel {
    private final JTextField field;

    public TextField(String label) {
        var jLabel = new JLabel(label);
        this.field = new JTextField();
        field.setSize(100, 30);

        setLayout(new GridLayout(2, 1, 10, 10));
        add(jLabel);
        add(field);
    }

    public JTextField getField() {
        return field;
    }
}
