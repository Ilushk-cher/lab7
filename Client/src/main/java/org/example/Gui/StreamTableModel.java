package org.example.Gui;

import org.example.CollectionModel.HumanBeing;
import org.example.CommandSpace.Printable;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.*;

public class StreamTableModel extends AbstractTableModel {
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, GuiManager.getLocale());
    private String[] columnNames;
    private ArrayList<HumanBeing> data;
    private ArrayList<HumanBeing> filteredData;
    private int sortingColumn = 0;
    private boolean reversed = false;
    private FilterWorker filterWorker;

    public StreamTableModel(String[] columnNames, FilterWorker filterWorker) {
        this.columnNames = columnNames;
        this.filterWorker = filterWorker;
    }

    public void setDataVector(ArrayList<HumanBeing> data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
        this.filteredData = filter(data);
    }

    public void performSorting(int column) {
        reversed = sortingColumn == column && !reversed;
        sortingColumn = column;
        filteredData = filter(data);
    }

    public void performSorting() {
        filteredData = filter(data);
    }

    @Override
    public int getRowCount() {
        return filteredData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getValueAtRow(filteredData.get(rowIndex), columnIndex);
    }

    private ArrayList<HumanBeing> filter(ArrayList<HumanBeing> data) {
        if (Objects.isNull(sortingColumn)) return data;
        ArrayList<HumanBeing> sortedData = new ArrayList<>(data.stream()
                .sorted(Comparator.comparing(o -> sortingColumn < 0
                        ? (long) o.getId()
                        : getSortedFiledFloat(o, sortingColumn)))
                .filter(filterWorker.getPredicate())
                .toList());
        if (reversed) Collections.reverse(sortedData);
        return sortedData;
    }

    public Object getValueAtRow(HumanBeing o, int row) {
        return switch (row) {
            case 0 -> o.getId();
            case 1 -> o.getName();
            case 2 -> o.getCoordinates();
            case 3 -> dateFormat.format(o.getCreationDate());
            case 4 -> o.getRealHero();
            case 5 -> o.getHasToothpick();
            case 6 -> o.getImpactSpeed();
            case 7 -> o.getWeaponType();
            case 8 -> o.getMood();
            case 9 -> o.getCar().getName();
            case 10 -> o.getUserLogin();
            default -> throw new IllegalStateException("Unexpected value: " + row);
        };
    }

    public float getSortedFiledFloat(HumanBeing o, int column) {
        return switch (column) {
            case 0 -> o.getId();
            case 1 -> o.getName().length();
            case 2 -> (float) o.getCoordinates().getDistanceToCentre();
            case 3 -> o.getCreationDate().getTime();
            case 4 -> (o.getRealHero()) ? 1 : 0;
            case 5 -> (o.getHasToothpick()) ? 1 : 0;
            case 6 -> o.getImpactSpeed();
            case 7 -> o.getWeaponType().ordinal();
            case 8 -> (o.getMood() == null) ? -1 : o.getMood().ordinal();
            case 9 -> o.getCar().getName().length();
            case 10 -> o.getUserLogin().length();
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };
    }

    public HumanBeing getRow(int row) {
        try {
            return filteredData.get(row);
        } catch (IndexOutOfBoundsException e) {
            return filteredData.get(0);
        }
    }

    public ArrayList<HumanBeing> getData() {
        return data;
    }
}
