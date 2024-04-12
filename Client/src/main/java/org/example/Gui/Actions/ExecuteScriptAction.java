package org.example.Gui.Actions;

import org.example.Client;
import org.example.CommandSpace.Console;
import org.example.Connection.User;
import org.example.Exceptions.ExitProgram;
import org.example.Gui.Components.ExecutionPanel;
import org.example.Gui.GuiManager;
import org.example.Managers.ExecuteFileManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.example.Gui.GuiManager.resourceBundle;

public class ExecuteScriptAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;

    public ExecuteScriptAction(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        JLabel asker = new JLabel(resourceBundle.getString("selectFile"));
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(asker)
                        .addComponent(fileChooser)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(asker)
                .addComponent(fileChooser));

        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                resourceBundle.getString("execute"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {
                        resourceBundle.getString("yes"),
                        resourceBundle.getString("no")
                },
                resourceBundle.getString("yes")
        );
        if (result == JOptionPane.OK_OPTION) {
            try {
                Console.setFileMode(true);
                ExecutionPanel executionPanel = new ExecutionPanel();
                JFrame frame = new JFrame(resourceBundle.getString("console"));
                frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(true);
                frame.setSize(700, 500);
                frame.setLocation(400, 300);
                frame.add(executionPanel);
                new ExecuteFileManager(user, client, new Console(executionPanel))
                        .executeFile(fileChooser.getSelectedFile().getAbsolutePath());
                Console.setFileMode(false);
                guiManager.refresh();
            } catch (ExitProgram ignored) {}
        }
    }
}
