package org.example.Gui.Components;

import org.example.Client;
import org.example.Connection.User;
import org.example.Gui.Actions.*;
import org.example.Gui.FilterListener;
import org.example.Gui.FilterWorker;
import org.example.Gui.GuiManager;
import org.example.Gui.StreamTableModel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.Gui.GuiManager.resourceBundle;

public class MenuBar extends JMenuBar {
    private final User user;
    private final Client client;
    private final GuiManager guiManager;
    private final ClassLoader classLoader;
    private final FilterWorker filterWorker;
    private final StreamTableModel tableModel;
    private final JTable table;

    public MenuBar(User user, Client client, GuiManager guiManager) {
        super();
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
        this.classLoader = guiManager.getClass().getClassLoader();
        this.tableModel = guiManager.getTableModel();
        this.table = guiManager.getTable();
        this.filterWorker = guiManager.getFilterWorker();
        build();
    }

    private void build() {
        int iconSize = 25;
        JMenu actions = new JMenu(resourceBundle.getString("actions"));
        JMenuItem add = new JMenuItem(resourceBundle.getString("add"));
        JMenuItem addIfMax = new JMenuItem(resourceBundle.getString("addIfMax"));
        JMenuItem clear = new JMenuItem(resourceBundle.getString("clear"));
        JMenuItem execute = new JMenuItem(resourceBundle.getString("execute"));
        JMenuItem exit = new JMenuItem(resourceBundle.getString("exit"));
        JMenuItem filterGreaterThanImpactSpeed = new JMenuItem(resourceBundle.getString("filterGreaterThanImpactSpeed"));
        JMenuItem info = new JMenuItem(resourceBundle.getString("info"));
        JMenuItem printUniqueMood = new JMenuItem(resourceBundle.getString("printUniqueMood"));
        JMenuItem remove = new JMenuItem(resourceBundle.getString("remove"));
        JMenuItem sumOfImpactSpeed = new JMenuItem(resourceBundle.getString("sumOfImpactSpeed"));
        JMenuItem update = new JMenuItem(resourceBundle.getString("update"));
        JMenuItem help = new JMenuItem(resourceBundle.getString("doHelp"));
        JMenuItem language = new JMenuItem(resourceBundle.getString("language"));

        //add all actions
        add.addActionListener(new AddAction(user, client, guiManager));
        addIfMax.addActionListener(new AddIfMaxAction(user, client, guiManager));
        clear.addActionListener(new ClearAction(user, client, guiManager));
        execute.addActionListener(new ExecuteScriptAction(user, client, guiManager));
        exit.addActionListener(new ExitAction(guiManager));
        filterGreaterThanImpactSpeed.addActionListener(new FilterGreaterThanImpactSpeedAction(user, client, guiManager));
        info.addActionListener(new InfoAction(user, client));
        printUniqueMood.addActionListener(new PrintUniqueMoodAction(user, client));
        remove.addActionListener(new RemoveAction(user, client, guiManager));
        sumOfImpactSpeed.addActionListener(new SumOfImpactSpeedAction(user, client));
        update.addActionListener(new UpdateAction(user, client,guiManager));
        help.addActionListener(new HelpAction(user, client));
        language.addActionListener(new ChangeLanguageAction(user, client, guiManager));

        add.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/addIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        addIfMax.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/addIfMaxIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        clear.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/clearIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        execute.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/executeIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        exit.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/exitIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        filterGreaterThanImpactSpeed.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/filterIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        info.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/infoIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        printUniqueMood.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/uniqueIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        remove.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/removeIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        sumOfImpactSpeed.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/sumOfIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        update.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/updateIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        language.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/languageIcon.png"))
                .getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));

        actions.add(add);
        actions.add(addIfMax);
        actions.add(clear);
        actions.add(execute);
        actions.add(exit);
        actions.add(filterGreaterThanImpactSpeed);
        actions.add(info);
        actions.add(printUniqueMood);
        actions.add(remove);
        actions.add(sumOfImpactSpeed);
        actions.add(update);
        actions.add(help);
        actions.add(language);

        this.add(actions);

        //now filters
        JMenuItem clearFilters = new JMenuItem(resourceBundle.getString("clearFilters"));
        JMenuItem idFilter = new JMenuItem(resourceBundle.getString("id"));
        JMenuItem nameFilter = new JMenuItem(resourceBundle.getString("name"));
        JMenuItem coordFilter = new JMenuItem(resourceBundle.getString("coord"));
        JMenuItem creationDateFilter = new JMenuItem(resourceBundle.getString("creationDate"));
        JMenuItem realHeroFilter = new JMenuItem(resourceBundle.getString("realHero"));
        JMenuItem hasToothpickFilter = new JMenuItem(resourceBundle.getString("hasToothpick"));
        JMenuItem impactSpeedFilter = new JMenuItem(resourceBundle.getString("impactSpeed"));
        JMenuItem weaponTypeFilter = new JMenuItem(resourceBundle.getString("weaponType"));
        JMenuItem moodFilter = new JMenuItem(resourceBundle.getString("mood"));
        JMenuItem carFilter = new JMenuItem(resourceBundle.getString("car"));
        JMenuItem ownerLoginFilter = new JMenuItem(resourceBundle.getString("ownerLogin"));

        clearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterWorker.clearPredicates();
                tableModel.performSorting();
                table.repaint();
                guiManager.refresh();
            }
        });
        idFilter.addActionListener(new FilterListener(0, tableModel, table, filterWorker));
        nameFilter.addActionListener(new FilterListener(1, tableModel, table, filterWorker));
        coordFilter.addActionListener(new FilterListener(2, tableModel, table, filterWorker));
        creationDateFilter.addActionListener(new FilterListener(3, tableModel, table, filterWorker));
        realHeroFilter.addActionListener(new FilterListener(4, tableModel, table, filterWorker));
        hasToothpickFilter.addActionListener(new FilterListener(5, tableModel, table, filterWorker));
        impactSpeedFilter.addActionListener(new FilterListener(6, tableModel, table, filterWorker));
        weaponTypeFilter.addActionListener(new FilterListener(7, tableModel, table, filterWorker));
        moodFilter.addActionListener(new FilterListener(8, tableModel, table, filterWorker));
        carFilter.addActionListener(new FilterListener(9, tableModel, table, filterWorker));
        ownerLoginFilter.addActionListener(new FilterListener(10, tableModel, table, filterWorker));

        JMenu filters = new JMenu(resourceBundle.getString("filters"));

        filters.add(clearFilters);
        filters.add(idFilter);
        filters.add(nameFilter);
        filters.add(coordFilter);
        filters.add(creationDateFilter);
        filters.add(realHeroFilter);
        filters.add(hasToothpickFilter);
        filters.add(impactSpeedFilter);
        filters.add(weaponTypeFilter);
        filters.add(moodFilter);
        filters.add(carFilter);
        filters.add(ownerLoginFilter);

        this.add(filters);
    }
}
