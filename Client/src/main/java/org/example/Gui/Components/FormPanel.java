package org.example.Gui.Components;

import org.example.CollectionModel.HumanBeing;
import org.example.CollectionModel.Parametres.Car;
import org.example.CollectionModel.Parametres.Coordinates;
import org.example.CollectionModel.Parametres.Mood;
import org.example.CollectionModel.Parametres.WeaponType;
import org.example.Connection.User;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import static org.example.Gui.GuiManager.*;

public class FormPanel extends JPanel {
    JLabel nameLabel = new JLabel(resourceBundle.getString("name"));
    JLabel coordXLabel = new JLabel(resourceBundle.getString("coordX"));
    JLabel coordYlabel = new JLabel(resourceBundle.getString("coordY"));
    JLabel realHeroLabel = new JLabel(resourceBundle.getString("realHero"));
    JLabel hasToothpickLabel = new JLabel(resourceBundle.getString("hasToothpick"));
    JLabel impactSpeedLabel = new JLabel(resourceBundle.getString("impactSpeed"));
    JLabel weaponTypeLabel = new JLabel(resourceBundle.getString("weaponType"));
    JLabel moodLabel = new JLabel(resourceBundle.getString("mood"));
    JLabel carLabel = new JLabel(resourceBundle.getString("car"));

    JLabel errorName = new JLabel(resourceBundle.getString("inputWait"));
    JLabel errorCoordX = new JLabel(resourceBundle.getString("inputWait"));
    JLabel errorCoordY = new JLabel(resourceBundle.getString("inputWait"));
    JLabel errorImpactSpeed = new JLabel(resourceBundle.getString("inputWait"));
    JLabel errorCar = new JLabel(resourceBundle.getString("inputWait"));

    JFormattedTextField nameField;
    JFormattedTextField coordXField;
    JFormattedTextField coordYField;
    JCheckBox realHeroField;
    JCheckBox hasToothpickField;
    JFormattedTextField impactSpeedField;
    JComboBox<WeaponType> weaponTypeField;
    JComboBox<Mood> moodField;
    JFormattedTextField carField;


    public FormPanel() {
        super();
        build();
    }

    private void build() {
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setVisible(true);

        errorName.setForeground(OTHER);
        errorCoordX.setForeground(OTHER);
        errorCoordY.setForeground(OTHER);
        errorImpactSpeed.setForeground(OTHER);
        errorCar.setForeground(OTHER);

        nameField = new JFormattedTextField(new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                errorName.setText("Ok");
                errorName.setForeground(OK);
                if (string.trim().isEmpty()) {
                    errorName.setText(resourceBundle.getString("fieldNotEmpty"));
                    errorName.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("fieldNotEmpty"), 0);
                }
                return super.stringToValue(string);
            }
        });
        coordXField = new JFormattedTextField(new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                int num;
                try {
                    num = Integer.parseInt(string);
                    errorCoordX.setText("Ok");
                    errorCoordX.setForeground(OK);
                } catch (NumberFormatException e) {
                    errorCoordX.setText(resourceBundle.getString("numberType") + " int");
                    errorCoordX.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("numberType") + " int", 0);
                }
                return num;
            }
        });
        coordYField = new JFormattedTextField(new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                double num;
                try {
                    num = Double.parseDouble(string.replace(",", "."));
                    errorCoordY.setText("Ok");
                    errorCoordY.setForeground(OK);
                } catch (NumberFormatException e) {
                    errorCoordY.setText(resourceBundle.getString("numberType") + " double");
                    errorCoordY.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("numberType") + " double", 0);
                }
                if (num <= -157) {
                    errorCoordY.setText(resourceBundle.getString("numberMustBe") + " > 157");
                    errorCoordY.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("numberMustBe") + " > 157", 0);
                }
                return num;
            }
        });
        realHeroField = new JCheckBox();
        hasToothpickField = new JCheckBox();
        impactSpeedField = new JFormattedTextField(new DefaultFormatter() {
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
        weaponTypeField = new JComboBox<>(WeaponType.values());
        moodField = new JComboBox<>(Mood.values());
        moodField.addItem(null);
        moodField.setSelectedItem(null);
        carField = new JFormattedTextField(new DefaultFormatter() {
            @Override
            public Object stringToValue(String string) throws ParseException {
                errorCar.setText("Ok");
                errorCar.setForeground(OK);
                if (string.trim().isEmpty()) {
                    errorCar.setText(resourceBundle.getString("fieldNotEmpty"));
                    errorCar.setForeground(WARNING);
                    throw new ParseException(resourceBundle.getString("fieldNotEmpty"), 0);
                }
                return super.stringToValue(string);
            }
        });

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(nameField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(errorName))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(coordXLabel)
                        .addComponent(coordXField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(errorCoordX))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(coordYlabel)
                        .addComponent(coordYField))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(errorCoordY))
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
                        .addComponent(errorImpactSpeed))
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
                        .addComponent(errorCar))
        );
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(coordXLabel)
                        .addComponent(coordYlabel)
                        .addComponent(realHeroLabel)
                        .addComponent(hasToothpickLabel)
                        .addComponent(impactSpeedLabel)
                        .addComponent(weaponTypeLabel)
                        .addComponent(moodLabel)
                        .addComponent(carLabel))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(nameField)
                        .addComponent(errorName)
                        .addComponent(coordXField)
                        .addComponent(errorCoordX)
                        .addComponent(coordYField)
                        .addComponent(errorCoordY)
                        .addComponent(realHeroField)
                        .addComponent(hasToothpickField)
                        .addComponent(impactSpeedField)
                        .addComponent(errorImpactSpeed)
                        .addComponent(weaponTypeField)
                        .addComponent(moodField)
                        .addComponent(carField)
                        .addComponent(errorCar))
        );
    }

    public boolean checkFields() {
        if (Objects.equals(errorName.getText(), "Ok")
                && Objects.equals(errorCoordX.getText(), "Ok")
                && Objects.equals(errorCoordY.getText(), "Ok")
                && Objects.equals(errorImpactSpeed.getText(), "Ok")
                && Objects.equals(errorCar.getText(), "Ok")
            ) {
            return true;
        } else {
            nameField.setValue(nameField.getText());
            coordXField.setValue(coordXField.getText());
            coordYField.setValue(coordYField.getText());
            realHeroField.setSelected(realHeroField.isSelected());
            hasToothpickField.setSelected(hasToothpickField.isSelected());
            impactSpeedField.setValue(impactSpeedField.getText());
            weaponTypeField.setSelectedItem(weaponTypeField.getSelectedItem());
            moodField.setSelectedItem(moodField.getSelectedItem());
            carField.setValue(carField.getText());
        }
        JOptionPane.showMessageDialog(
                null,
                resourceBundle.getString("invalidInput"),
                resourceBundle.getString("error"),
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public HumanBeing createHumanBeing(User user) {
        if (!checkFields()) return null;
        return new HumanBeing(
                getNameField(),
                new Coordinates(
                        getCoordXField(),
                        getCoordYField()
                ),
                new Date(),
                getRealHeroField(),
                getHasToothpickField(),
                getImpactSpeedField(),
                getWeaponTypeField(),
                getMoodField(),
                new Car(getCarField()),
                user.getLogin()
        );
    }

    public HumanBeing createHumanBeing(User user, Date date) {
        if (!checkFields()) return null;
        return new HumanBeing(
                getNameField(),
                new Coordinates(
                        getCoordXField(),
                        getCoordYField()
                ),
                date,
                getRealHeroField(),
                getHasToothpickField(),
                getImpactSpeedField(),
                getWeaponTypeField(),
                getMoodField(),
                new Car(getCarField()),
                user.getLogin()
        );
    }

    public void setDefaultValues(HumanBeing humanBeing) {
        nameField.setValue(humanBeing.getName());
        coordXField.setValue(humanBeing.getCoordinates().getX());
        coordYField.setValue(humanBeing.getCoordinates().getY());
        realHeroField.setSelected(humanBeing.getRealHero());
        hasToothpickField.setSelected(humanBeing.getHasToothpick());
        impactSpeedField.setValue(humanBeing.getImpactSpeed());
        weaponTypeField.setSelectedItem(humanBeing.getWeaponType());
        moodField.setSelectedItem(humanBeing.getMood());
        carField.setValue(humanBeing.getCar().getName());

        errorName.setText("Ok");
        errorName.setForeground(OK);
        errorCoordX.setText("Ok");
        errorCoordX.setForeground(OK);
        errorCoordY.setText("Ok");
        errorCoordY.setForeground(OK);
        errorImpactSpeed.setText("Ok");
        errorImpactSpeed.setForeground(OK);
        errorCar.setText("Ok");
        errorCar.setForeground(OK);
    }

    private String getNameField() {
        return nameField.getText();
    }

    private int getCoordXField() {
        return Integer.parseInt(coordXField.getText());
    }

    private double getCoordYField() {
        return Double.parseDouble(coordYField.getText());
    }

    private boolean getRealHeroField() {
        return realHeroField.isSelected();
    }

    private boolean getHasToothpickField() {
        return hasToothpickField.isSelected();
    }

    private int getImpactSpeedField() {
        return Integer.parseInt(impactSpeedField.getText());
    }

    private WeaponType getWeaponTypeField() {
        return (WeaponType) weaponTypeField.getSelectedItem();
    }

    private Mood getMoodField() {
        return (Mood) moodField.getSelectedItem();
    }

    private String getCarField() {
        return carField.getText();
    }
}
