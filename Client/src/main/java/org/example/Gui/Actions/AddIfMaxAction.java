package org.example.Gui.Actions;

import org.example.Client;
import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.Components.FormPanel;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.OK_OPTION;
import static org.example.Gui.GuiManager.resourceBundle;

public class AddIfMaxAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;
    public AddIfMaxAction(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FormPanel formPanel = new FormPanel();
        int result = JOptionPane.showOptionDialog(
                null,
                formPanel,
                resourceBundle.getString("addIfMax"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{resourceBundle.getString("addIfMax")},
                resourceBundle.getString("addIfMax")
        );
        if (result == OK_OPTION) {
            HumanBeing humanBeing = formPanel.createHumanBeing(user);
            if (!humanBeing.validate()) {
                JOptionPane.showMessageDialog(
                        null,
                        resourceBundle.getString("objInvalid"),
                        resourceBundle.getString("error"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            Response response = client.getResponse(new Request("add_if_max", "", user, humanBeing, GuiManager.getLocale()));
            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(
                        null,
                        resourceBundle.getString("objAcc"),
                        resourceBundle.getString("result"),
                        JOptionPane.PLAIN_MESSAGE
                );
            } else JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("error"),
                    resourceBundle.getString("lessObject"),
                    JOptionPane.ERROR_MESSAGE
            );
            guiManager.refresh();
        }
    }
}
