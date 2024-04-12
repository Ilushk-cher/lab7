package org.example.Gui.Components;

import org.example.Client;
import org.example.CollectionModel.HumanBeing;
import org.example.CollectionModel.Parametres.Coordinates;
import org.example.CollectionModel.Parametres.Mood;
import org.example.CollectionModel.Parametres.WeaponType;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Gui.GuiManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Gui.GuiManager.resourceBundle;

public class ObjectsPanel extends JPanel implements ActionListener {
    private GuiManager guiManager;
    private LinkedHashMap<Rectangle, Integer> rectangles;
    private Timer timer;
    private Map<String, Color> users;
    private int step;
    private Collection<HumanBeing> collection;

    private int maxCoordX;
    private double maxCoordY;
    private BufferedImage image = null;
    private boolean skipAnimation = false;

    {
        try {
            this.image = ImageIO.read(getClass().getClassLoader().getResource("icons/humanBeing.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectsPanel(GuiManager guiManager) {
        super();
        this.guiManager = guiManager;
        this.step = 0;
        this.timer = new Timer(3, this);
        timer.start();
        updateUserObjects();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Rectangle clicked;
                try {
                    clicked = rectangles.keySet().stream()
                            .filter(i -> i.contains(e.getPoint()))
                            .sorted(Comparator.comparing(Rectangle::getX).reversed())
                            .toList().get(0);
                } catch (ArrayIndexOutOfBoundsException er) {
                    return;
                }
                Long id = (long) rectangles.get(clicked);
                HumanBeing humanBeing = guiManager.getCollection().stream()
                        .filter(s -> s.getId().equals(id))
                        .toList().get(0);
                showInfo(humanBeing);
            }
        });
    }

    public void updateUserObjects() {
        Random random = new Random();
        users = guiManager.getCollection().stream()
                .map(HumanBeing::getUserLogin)
                .distinct()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> {
                            int red = random.nextInt(25) * 10;
                            int green = random.nextInt(25) * 10;
                            int blue = random.nextInt(25) * 10;
                            return new Color(red, green, blue);
                        }
                ));
        collection = guiManager.getCollection();
        maxCoordX = collection.stream()
                .map(HumanBeing::getCoordinates)
                .map(Coordinates::getX)
                .map(Math::abs)
                .max(Integer::compareTo)
                .orElse(0);
        maxCoordY = collection.stream()
                .map(HumanBeing::getCoordinates)
                .map(Coordinates::getY)
                .map(Math::abs)
                .max(Double::compareTo)
                .orElse(0D);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        int width = getWidth();
        int height = getHeight();

        g2.drawLine(0, height / 2, width, height / 2); // x
        g2.drawLine(width / 2, 0, width / 2, height); // y

        g2.drawLine(width - 8, height / 2 - 3, width, height / 2);
        g2.drawLine(width - 8, height / 2 + 3, width, height / 2);
        g2.drawLine(width / 2 - 3, height - 8, width / 2, height);
        g2.drawLine(width / 2 + 3, height - 8, width / 2, height);

        paintRectangles(g2);
    }

    private void paintRectangles(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();
        int elWidth = 50;
        int elHeight = 50;

        if (skipAnimation) {
            step = 100;
            skipAnimation = false;
        }

        if (step == 100) {
            rectangles = new LinkedHashMap<>();
        }

        collection.stream().sorted(HumanBeing::compareTo).forEach(humanBeing -> {
            float objX = (float) humanBeing.getCoordinates().getX();
            float objY = (float) humanBeing.getCoordinates().getY();
            int dx = (int) (width / 2 + (objX * step / 100 / maxCoordX * (width / 2 - elWidth)));
            int dy = (int) (height / 2 + (objY * step / 100 / maxCoordY * (height / 2 - elHeight)));

            if (step == 100) {
                rectangles.put(new Rectangle(
                        dx - elWidth / 2 - 1,
                        dy - elHeight / 2 - 1,
                        elWidth + 2,
                        elHeight + 2), Integer.parseInt(humanBeing.getId().toString()));
            }

            g2.drawImage(
                    image,
                    dx - elWidth / 2,
                    dy - elHeight / 2,
                    dx + elWidth / 2,
                    dy + elHeight / 2,
                    0,
                    0,
                    image.getWidth(),
                    image.getHeight(),
                    null);

            g2.setColor(users.get(humanBeing.getUserLogin()));
            g2.drawRect(
                    dx - elWidth / 2 - 1,
                    dy - elHeight / 2 - 1,
                    elWidth + 2,
                    elHeight + 2
            );
            g2.setColor(Color.WHITE);
            g2.drawString(
                    humanBeing.getId().toString(),
                    dx - 7,
                    dy + 2
            );
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (step == 100) timer.stop();
        else {
            step += 1;
            step = Math.min(step, 100);
            repaint();
        }
    }

    public void reanimate() {
        step = 0;
        timer.start();
    }

    private void showInfo(HumanBeing humanBeing) {
        JLabel idLabel = new JLabel(resourceBundle.getString("id") + ": ");
        JLabel nameLabel = new JLabel(resourceBundle.getString("name") + ": ");
        JLabel coordXLabel = new JLabel(resourceBundle.getString("coordX") + ": ");
        JLabel coordYlabel = new JLabel(resourceBundle.getString("coordY") + ": ");
        JLabel creationDateLabel = new JLabel(resourceBundle.getString("creationDate") + ": ");
        JLabel realHeroLabel = new JLabel(resourceBundle.getString("realHero") + ": ");
        JLabel hasToothpickLabel = new JLabel(resourceBundle.getString("hasToothpick") + ": ");
        JLabel impactSpeedLabel = new JLabel(resourceBundle.getString("impactSpeed") + ": ");
        JLabel weaponTypeLabel = new JLabel(resourceBundle.getString("weaponType") + ": ");
        JLabel moodLabel = new JLabel(resourceBundle.getString("mood") + ": ");
        JLabel carLabel = new JLabel(resourceBundle.getString("car") + ": ");
        JLabel ownerLoginLabel = new JLabel(resourceBundle.getString("ownerLogin") + ": ");

        JLabel idField = new JLabel(String.valueOf(humanBeing.getId()));
        JLabel nameField = new JLabel(humanBeing.getName());
        JLabel coordXField = new JLabel(String.valueOf(humanBeing.getCoordinates().getX()));
        JLabel coordYField = new JLabel(String.valueOf(humanBeing.getCoordinates().getY()));
        JLabel creationDateField = new JLabel(DateFormat.getDateInstance(DateFormat.MEDIUM, GuiManager.getLocale()).format(humanBeing.getCreationDate()));
        JLabel realHeroField = new JLabel(humanBeing.getRealHero().toString());
        JLabel hasToothpickField = new JLabel(humanBeing.getHasToothpick().toString());
        JLabel impactSpeedField = new JLabel(humanBeing.getImpactSpeed().toString());
        JLabel weaponTypeField = new JLabel(humanBeing.getWeaponType().toString());
        JLabel moodField = new JLabel((humanBeing.getMood() == null) ? null : humanBeing.getMood().toString());
        JLabel carField = new JLabel(humanBeing.getCar().getName());
        JLabel ownerLoginField = new JLabel(humanBeing.getUserLogin());

        JPanel infoPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(infoPanel);
        infoPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        infoPanel.setVisible(true);
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(idLabel)
                        .addComponent(idField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(nameField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(coordXLabel)
                        .addComponent(coordXField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(coordYlabel)
                        .addComponent(coordYField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(creationDateLabel)
                        .addComponent(creationDateField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(realHeroLabel)
                        .addComponent(realHeroField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(hasToothpickLabel)
                        .addComponent(hasToothpickField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(impactSpeedLabel)
                        .addComponent(impactSpeedField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(weaponTypeLabel)
                        .addComponent(weaponTypeField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(moodLabel)
                        .addComponent(moodField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(carLabel)
                        .addComponent(carField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(ownerLoginLabel)
                        .addComponent(ownerLoginField))
        );
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(idLabel)
                        .addComponent(nameLabel)
                        .addComponent(coordXLabel)
                        .addComponent(coordYlabel)
                        .addComponent(creationDateLabel)
                        .addComponent(realHeroLabel)
                        .addComponent(hasToothpickLabel)
                        .addComponent(impactSpeedLabel)
                        .addComponent(weaponTypeLabel)
                        .addComponent(moodLabel)
                        .addComponent(carLabel)
                        .addComponent(ownerLoginLabel))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(idField)
                        .addComponent(nameField)
                        .addComponent(coordXField)
                        .addComponent(coordYField)
                        .addComponent(creationDateField)
                        .addComponent(realHeroField)
                        .addComponent(hasToothpickField)
                        .addComponent(impactSpeedField)
                        .addComponent(weaponTypeField)
                        .addComponent(moodField)
                        .addComponent(carField)
                        .addComponent(ownerLoginField)
        ));

        JOptionPane.showMessageDialog(
                null,
                infoPanel
        );
    }
}
