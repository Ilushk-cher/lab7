package org.example.Gui.Actions;

import org.example.Client;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {
    private final GuiManager guiManager;

    public ExitAction(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        guiManager.reLogin();
    }
}
