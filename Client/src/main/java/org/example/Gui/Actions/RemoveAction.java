package org.example.Gui.Actions;

import org.example.Client;
import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

import static org.example.Gui.GuiManager.resourceBundle;

public class RemoveAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;

    public RemoveAction(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    private Long getSelectedId() {
        Long[] userIds = guiManager.getCollection().stream()
                .filter(s -> s.getUserLogin().equals(user.getLogin()))
                .map(HumanBeing::getId)
                .toArray(Long[]::new);

        BorderLayout borderLayout = new BorderLayout();
        JPanel panel = new JPanel(borderLayout);
        JLabel question = new JLabel(resourceBundle.getString("selectId"));
        JLabel idLabel = new JLabel(resourceBundle.getString("selectId"));
        JComboBox idField = new JComboBox(userIds);
        idField.setSelectedItem(null);

        borderLayout.addLayoutComponent(question, BorderLayout.NORTH);
        borderLayout.addLayoutComponent(idLabel, BorderLayout.WEST);
        borderLayout.addLayoutComponent(idField, BorderLayout.EAST);

        JOptionPane.showMessageDialog(
                null,
                idField,
                resourceBundle.getString("remove"),
                JOptionPane.PLAIN_MESSAGE
        );
        if (idField.getSelectedItem() == null) {
            return null;
        }
        return Long.parseLong(Objects.requireNonNull(idField.getSelectedItem()).toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Long id = getSelectedId();
        removeObject(id);
    }

    public void removeObject(Long id) {
        if (id == null) {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("noObj"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        Response response = client.getResponse(new Request("remove_by_id", id.toString(), user, GuiManager.getLocale()));
        if (response.getResponseStatus() == ResponseStatus.OK) {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("objDeleted"),
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("objNotDeleted"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
