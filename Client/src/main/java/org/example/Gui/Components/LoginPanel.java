package org.example.Gui.Components;

import org.example.Gui.GuiManager;

import javax.swing.*;

import java.awt.*;

import static org.example.Gui.GuiManager.resourceBundle;


public class LoginPanel extends JPanel {
    JLabel loginTextLabel = new JLabel(resourceBundle.getString("enterLogin"));
    JTextField loginField = new JTextField(15);
    JLabel passwordTextLabel = new JLabel(resourceBundle.getString("enterPassword"));
    JPasswordField passwordField = new JPasswordField(15);
    JLabel errorLabel = new JLabel("");


    public LoginPanel() {
        super();
        build();
    }

    private void build() {
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setVisible(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginTextLabel)
                        .addComponent(passwordTextLabel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginField)
                        .addComponent(passwordField)
                        .addComponent(errorLabel)
                )
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginTextLabel)
                        .addComponent(loginField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordTextLabel)
                        .addComponent(passwordField)
                )
                .addComponent(errorLabel)
        );
        add(errorLabel);
    }

    public boolean isInvalidFields() {
        if (loginField.getText().isEmpty()) {
            errorLabel.setText(resourceBundle.getString("notNullLogin"));
            errorLabel.setForeground(GuiManager.WARNING);

            return true;
        } else if (String.valueOf(passwordField.getPassword()).isEmpty()) {
            errorLabel.setText(resourceBundle.getString("notNullPassword"));
            errorLabel.setForeground(GuiManager.WARNING);
            return true;
        }
        return false;
    }

    public String getLogin() {
        return loginField.getText();
    }

    public String getPassword() {
        return String.valueOf(passwordField.getPassword());
    }

    public void setError(String message, Color color) {
        errorLabel.setText(message);
        errorLabel.setForeground(color);
    }
}
