package org.example.Gui.Actions;

import org.example.Client;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import static org.example.Gui.GuiManager.*;

public class FilterGreaterThanImpactSpeedAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;

    public FilterGreaterThanImpactSpeedAction(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JLabel errorImpactSpeed = new JLabel(resourceBundle.getString("inputWait"));
        errorImpactSpeed.setForeground(OTHER);
        JFormattedTextField impactSpeedField = new JFormattedTextField(new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                int num;
                try {
                    num = Integer.parseInt(string);
                    errorImpactSpeed.setText("Ok");
                    errorImpactSpeed.setForeground(OK);
                } catch (NumberFormatException e) {
                    errorImpactSpeed.setText(resourceBundle.getString("numberType") + " int");
                    errorImpactSpeed.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("numberType") + " int", 0);
                }
                return num;
            }
        });

        JPanel questionPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(questionPanel);
        questionPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        questionPanel.setVisible(true);

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(impactSpeedField)
                .addComponent(errorImpactSpeed)
        );
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(impactSpeedField)
                .addComponent(errorImpactSpeed));

        JOptionPane.showMessageDialog(
                null,
                questionPanel,
                resourceBundle.getString("impactSpeed"),
                JOptionPane.QUESTION_MESSAGE
        );
        if (!Objects.equals(errorImpactSpeed.getText(), "Ok")) {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("invalidInput"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Response response = client.getResponse(new Request("filter_greater_than_impact_speed", impactSpeedField.getText(), user, GuiManager.getLocale()));
        if (response.getResponseStatus() == ResponseStatus.OK) {
            guiManager.getTableModel().setDataVector(new ArrayList<>(response.getCollection()), guiManager.getColumnNames());
            guiManager.setTableData(new ArrayList<>(response.getCollection()));
            JOptionPane.showMessageDialog(null, resourceBundle.getString("filteredGreaterThanImpactSpeed"));
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    resourceBundle.getString("error"),
                    resourceBundle.getString("error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
