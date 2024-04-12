package org.example.Gui.Components;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static org.example.Gui.GuiManager.WARNING;
import static org.example.Gui.GuiManager.resourceBundle;

public class ExecutionPanel extends JPanel {
    JTextArea consoleText = new JTextArea(resourceBundle.getString("start"));
    JLabel errorText = new JLabel();

    public ExecutionPanel() {
        super();
        build();
    }

    private void build() {
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setVisible(true);

        errorText.setForeground(WARNING);

        JScrollPane scrollPaneConsole = new JScrollPane(consoleText);

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(scrollPaneConsole)
                .addComponent(errorText));
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(scrollPaneConsole)
                        .addComponent(errorText)));
    }

    public void printConsoleText(String message) {
        consoleText.setText(consoleText.getText() + "\n" + message);
    }

    public void printErrorText(String message) {
        errorText.setText(errorText.getText() + "\n" + message);
    }
}
