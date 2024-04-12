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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static org.example.Gui.GuiManager.resourceBundle;

public class UpdateAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;
    public UpdateAction(User user, Client client, GuiManager guiManager) {
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
                resourceBundle.getString("update"),
                JOptionPane.PLAIN_MESSAGE
        );
        if (idField.getSelectedItem() == null) {
            return null;
        }
        return Long.parseLong(Objects.requireNonNull(idField.getSelectedItem()).toString());
    }

    private HumanBeing getObject(long id) {
        return guiManager.getCollection().stream()
                .filter(s -> s.getId().equals(id))
                .toList().get(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Long id = getSelectedId();
        updateObject(id);
    }

    public void updateObject(Long id) {
        if (id == null) {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("noObj"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        FormPanel formPanel = new FormPanel();
        HumanBeing oldHumanBeing = getObject(id);
        formPanel.setDefaultValues(oldHumanBeing);
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    formPanel,
                    resourceBundle.getString("update") + " id: " + id,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[]{resourceBundle.getString("update")},
                    resourceBundle.getString("update")
            );
            if (result == OK_OPTION) {
                HumanBeing newHumanBeing = formPanel.createHumanBeing(user, oldHumanBeing.getCreationDate());
                if (newHumanBeing == null) continue;
                if (!newHumanBeing.validate()) {
                    JOptionPane.showMessageDialog(
                            null,
                            resourceBundle.getString("objInvalid"),
                            resourceBundle.getString("error"),
                            JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }
                Response response = client.getResponse(new Request("update", id.toString(), user, newHumanBeing, GuiManager.getLocale()));
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    JOptionPane.showMessageDialog(
                            null,
                            resourceBundle.getString("objUpdated"),
                            resourceBundle.getString("result"),
                            JOptionPane.PLAIN_MESSAGE
                    );
                } else JOptionPane.showMessageDialog(
                        null,
                        resourceBundle.getString("objInvalid"),
                        resourceBundle.getString("error"),
                        JOptionPane.ERROR_MESSAGE
                );
                guiManager.refresh();
                break;
            } else if (result == CLOSED_OPTION) {
                return;
            }
        }

    }
}
