package org.example.Gui.Actions;

import org.example.Client;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Ref;

import static org.example.Gui.GuiManager.resourceBundle;

public class ClearAction extends AbstractAction {
    private final User user;
    private final Client client;
    private GuiManager guiManager;

    public ClearAction(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showOptionDialog(
                null,
                resourceBundle.getString("areYouSure"),
                resourceBundle.getString("confirmation"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {
                        resourceBundle.getString("yes"),
                        resourceBundle.getString("no")
                },
                resourceBundle.getString("no")
        );
        if (result == JOptionPane.OK_OPTION) {
            Response response = client.getResponse(new Request("clear", "", user, GuiManager.getLocale()));
            if (response.getResponseStatus() == ResponseStatus.OK) {
                JOptionPane.showMessageDialog(
                        null,
                        resourceBundle.getString("objDeleted"),
                        resourceBundle.getString("result"),
                        JOptionPane.PLAIN_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        resourceBundle.getString("objInvalid"),
                        resourceBundle.getString("error"),
                        JOptionPane.ERROR_MESSAGE
                );
            }
            guiManager.refresh();
        }
    }
}
