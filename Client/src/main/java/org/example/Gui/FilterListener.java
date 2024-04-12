package org.example.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Filter;

public class FilterListener implements ActionListener {
    private int row;
    private StreamTableModel tableModel;
    private FilterWorker filterWorker;
    private JTable table;

    public FilterListener(int row, StreamTableModel tableModel, JTable table, FilterWorker filterWorker) {
        this.row = row;
        this.tableModel = tableModel;
        this.table = table;
        this.filterWorker = filterWorker;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JList<?> jList = new JList<>(tableModel.getData().stream()
                .map(o -> tableModel.getValueAtRow(o, row))
                .distinct()
                .toArray());
        JScrollPane jScrollPane = new JScrollPane(jList);
        JOptionPane.showMessageDialog(null, jScrollPane);
        if (jList.getSelectedValuesList().isEmpty()) return;
        filterWorker.parsePredicate(row, jList.getSelectedValuesList());
        tableModel.performSorting();
        table.repaint();
    }
}
