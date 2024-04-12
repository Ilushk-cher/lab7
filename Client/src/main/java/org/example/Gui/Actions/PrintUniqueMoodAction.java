package org.example.Gui.Actions;

import org.example.Client;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.example.Gui.GuiManager.resourceBundle;

public class PrintUniqueMoodAction extends AbstractAction {
    private User user;
    private Client client;

    public PrintUniqueMoodAction(User user, Client client) {
        super();
        this.user = user;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Response response = client.getResponse(new Request("print_unique_mood", "", user, GuiManager.getLocale()));
        if (response.getResponseStatus() == ResponseStatus.OK) {
            JOptionPane.showMessageDialog(
                    null,
                    response.getResponse(),
                    resourceBundle.getString("uniqueMood"),
                    JOptionPane.PLAIN_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("noResult"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
