package org.example;

import org.example.Gui.Components.FormPanel;
import org.example.Gui.Components.LoginPanel;

import javax.swing.*;
import java.awt.*;

import static org.example.Gui.GuiManager.resourceBundle;

public class TestGui {
    public static void main(String[] args) {
        JFrame frame = new JFrame(resourceBundle.getString("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        FormPanel formPanel = new FormPanel();
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    formPanel,
                    resourceBundle.getString("login"),
                    JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{
                            resourceBundle.getString("loginButton"),
                    },
                    resourceBundle.getString("login")
            );
        }
    }
}
