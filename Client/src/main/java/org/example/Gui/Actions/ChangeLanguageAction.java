package org.example.Gui.Actions;

import org.example.Client;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class ChangeLanguageAction extends AbstractAction {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;

    public ChangeLanguageAction(User user, Client client, GuiManager guiManager) {
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox languages = new JComboBox(new Object[] {
                new Locale("ru", "RU"),
                new Locale("en_CA"),
                new Locale("pl"),
                new Locale("sl")
        });
        languages.setSelectedItem(GuiManager.getLocale());
        JOptionPane.showMessageDialog(
                null,
                languages,
                "Choose language",
                JOptionPane.INFORMATION_MESSAGE
        );
        guiManager.setLocale((Locale) languages.getSelectedItem());
    }
}
