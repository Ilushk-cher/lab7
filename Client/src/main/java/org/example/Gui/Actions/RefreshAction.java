package org.example.Gui.Actions;

import org.example.Gui.GuiManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RefreshAction extends AbstractAction {
    private final GuiManager guiManager;

    public RefreshAction(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        guiManager.refresh();
    }
}
