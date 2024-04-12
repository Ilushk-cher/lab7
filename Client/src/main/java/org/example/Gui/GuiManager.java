package org.example.Gui;

import org.example.Client;
import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.Actions.*;
import org.example.Gui.Components.LoginPanel;
import org.example.Gui.Components.MenuBar;
import org.example.Gui.Components.ObjectsPanel;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.*;

import static javax.swing.JOptionPane.*;

public class GuiManager {
    private final Client client;
    private static Locale locale = new Locale("ru");
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("Gui", getLocale());
    private JFrame frame;
    private JPanel panel;
    private JTable table = null;
    private StreamTableModel tableModel = null;
    private final FilterWorker filterWorker = new FilterWorker();
    private ObjectsPanel objectsPanel;

    private ArrayList<HumanBeing> tableData = null;
    private ArrayList<HumanBeing> collection = null;

    private User user;

    public final static Color WARNING = Color.RED;
    public final static Color OK = Color.decode("#306207");
    public final static Color OTHER = Color.GRAY;

    private int clicksCount = 0;

    private final String[] columnNames = {
            "id",
            "name",
            "coordinates",
            "creation_date",
            "real_hero",
            "has_toothpick",
            "impact_speed",
            "weapon_type",
            "mood",
            "car",
            "owner_login"
    };


    public GuiManager(Client client) {
        this.client = client;
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame(resourceBundle.getString("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(false);
        frame.setResizable(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        SwingUtilities.invokeLater(this::run);
    }

    public GuiManager(Client client, User user) {
        this.client = client;
        this.user = user;
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame(resourceBundle.getString("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(false);
        frame.setResizable(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        SwingUtilities.invokeLater(this::run);
    }

    public void run() {
        panel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        if (user == null) {
            login();
        }

        frame.setVisible(true);
        tableData = getTableData();
        tableModel = new StreamTableModel(columnNames, filterWorker);
        tableModel.setDataVector(tableData, columnNames);
        table = new JTable(tableModel);
        frame.setJMenuBar(new MenuBar(user, client, this));

        JButton tableExecute = new JButton(resourceBundle.getString("table"));
        JButton objectsExecute = new JButton(resourceBundle.getString("coords"));

        table.getTableHeader().setReorderingAllowed(false);
        table.setDragEnabled(false);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int column = table.getTableHeader().columnAtPoint(point);
                tableModel.performSorting(column);
                table.repaint();
            }
        });




        JScrollPane tablePane = new JScrollPane(table);
        objectsPanel = new ObjectsPanel(this);

        table.getSelectionModel().addListSelectionListener(e -> {
            clicksCount++;
            if (clicksCount == 2) {
                long id = tableModel.getRow(table.getSelectedRow()).getId();

                if (!getCollection().stream()
                        .filter(s -> s.getId().equals(id))
                        .toList()
                        .get(0)
                        .getUserLogin()
                        .equals(user.getLogin())) {
                    JOptionPane.showMessageDialog(
                            null,
                            resourceBundle.getString("notYourObj"),
                            resourceBundle.getString("error"),
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    int result = JOptionPane.showOptionDialog(
                            null,
                            resourceBundle.getString("whatYouWant"),
                            resourceBundle.getString("choose"),
                            YES_NO_OPTION,
                            PLAIN_MESSAGE,
                            null,
                            new String[] {
                                    resourceBundle.getString("update"),
                                    resourceBundle.getString("remove")
                            },
                            resourceBundle.getString("choose")
                    );
                    if (result == OK_OPTION) {
                        new UpdateAction(user, client, GuiManager.this).updateObject(id);
                    } else if (result == NO_OPTION) {
                        new RemoveAction(user, client, GuiManager.this).removeObject(id);
                    }
                    refresh();
                }
                clicksCount = 0;
            }
        });

        JPanel cardPanel = new JPanel();

        ImageIcon userIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/user.png"))
                .getImage().getScaledInstance(25, 25,Image.SCALE_AREA_AVERAGING));
        JLabel userLabel = new JLabel(user.getLogin());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        userLabel.setIcon(userIcon);

        JButton addButton = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/addIcon.png"))
                .getImage().getScaledInstance(25, 25, Image.SCALE_AREA_AVERAGING)));
        addButton.setContentAreaFilled(false);
        addButton.addActionListener(new AddAction(user, client, this));

        JButton refreshButton = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/refreshIcon.png"))
                .getImage().getScaledInstance(25, 25, Image.SCALE_AREA_AVERAGING)));
        refreshButton.setContentAreaFilled(false);
        refreshButton.addActionListener(new RefreshAction(this));

        JButton exitButton = new JButton(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/exitIcon.png"))
                .getImage().getScaledInstance(17,17, Image.SCALE_DEFAULT)));
        exitButton.setContentAreaFilled(false);
        exitButton.addActionListener(new ExitAction(this));

        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.add(tablePane,"Table");
        cardPanel.add(objectsPanel, "Objects");

        tableExecute.addActionListener(event -> cardLayout.show(cardPanel, "Table"));
        objectsExecute.addActionListener(event -> {
            objectsPanel.reanimate();
            cardLayout.show(cardPanel, "Objects");
        });

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(cardPanel)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(tableExecute)
                                .addComponent(objectsExecute)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addButton)
                                .addComponent(refreshButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(userLabel)
                                .addComponent(exitButton)
                                .addGap(5))));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(cardPanel)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(tableExecute)
                        .addComponent(objectsExecute)
                        .addComponent(addButton)
                        .addComponent(refreshButton)
                        .addComponent(userLabel)
                        .addComponent(exitButton)
                        .addGap(10)));


        frame.add(panel);
        frame.setVisible(true);
    }

    public void login() {
        LoginPanel loginPanel = new LoginPanel();
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    loginPanel,
                    resourceBundle.getString("login"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new String[] {
                            resourceBundle.getString("login"),
                            resourceBundle.getString("register")
                    },
                    resourceBundle.getString("login")
            );
            if (result == OK_OPTION) {
                if (loginPanel.isInvalidFields()) continue;
                Response response = client.getResponse(
                        new Request(
                                "ping",
                                "",
                                new User(loginPanel.getLogin(), loginPanel.getPassword()),
                                getLocale()
                        )
                );
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    loginPanel.setError(resourceBundle.getString("loginAccess"), OK);
                    this.user = new User(loginPanel.getLogin(), loginPanel.getPassword());
                    return;
                } else loginPanel.setError(resourceBundle.getString("loginNotAccess"), WARNING);
            } else if (result == NO_OPTION) {
                if (loginPanel.isInvalidFields()) continue;
                Response response = client.getResponse(
                        new Request(
                                "register",
                                "",
                                new User(loginPanel.getLogin(), loginPanel.getPassword()),
                                getLocale()
                        )
                );
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    loginPanel.setError(resourceBundle.getString("registerAccess"), OK);
                    this.user = new User(loginPanel.getLogin(), loginPanel.getPassword());
                    return;
                } else loginPanel.setError(resourceBundle.getString("registerNotAccess"), WARNING);
            } else if (result == CLOSED_OPTION) {
                System.exit(2);
            }
        }
    }

    public void reLogin() {
        frame.removeAll();
        user = null;
        frame.setVisible(false);
        frame = new JFrame(resourceBundle.getString("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        SwingUtilities.invokeLater(this::run);
    }

    public ArrayList<HumanBeing> getTableData() {
        Response response = client.getResponse(new Request("show", "", user, getLocale()));
        if (response.getResponseStatus() != ResponseStatus.OK) return null;
        collection = new ArrayList<>(response.getCollection());
        return new ArrayList<>(response.getCollection());

    }

    public static Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        System.out.println(locale);
        GuiManager.locale = locale;
        ResourceBundle.clearCache();
        resourceBundle = ResourceBundle.getBundle("Gui", locale);
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        tableData = getTableData();
        tableModel.fireTableDataChanged();
        frame.remove(panel);
        frame.setTitle(resourceBundle.getString("title"));
        run();
    }

    public FilterWorker getFilterWorker() {
        return filterWorker;
    }

    public StreamTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public ArrayList<HumanBeing> getCollection() {
        return collection;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setTableData(ArrayList<HumanBeing> tableData) {
        this.tableData = tableData;
    }

    public void refresh() {
        ArrayList<HumanBeing> newTableData = getTableData();
        if (!(tableData.equals(newTableData))) {
            tableData = newTableData;
            tableModel.setDataVector(tableData, columnNames);
            tableModel.performSorting();
            table.repaint();
            tableModel.fireTableDataChanged();
            objectsPanel.updateUserObjects();
            objectsPanel.reanimate();
        }
    }
}
