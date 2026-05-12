package com.cityflow.gui;

import com.cityflow.controller.CentralController;
import com.cityflow.model.Intersection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sortable-style grid registry of all intersections with live telemetry.
 */
public class IntersectionRegistryPanel extends JPanel {
    private final CentralController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel selectionDetail;

    public IntersectionRegistryPanel(CentralController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        setBackground(UiTheme.BG_DEEP);

        String[] cols = {"ID", "X", "Y", "Signal", "Queue", "Congestion %", "Green ms", "Processed"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(UiTheme.fontMono(Font.PLAIN, 12));
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(UiTheme.fontUi(Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(true);

        selectionDetail = new JLabel(" ");
        selectionDetail.setFont(UiTheme.fontUi(Font.PLAIN, 13));
        selectionDetail.setForeground(UiTheme.TEXT);
        selectionDetail.setBackground(UiTheme.BG_CARD);
        selectionDetail.setOpaque(true);
        selectionDetail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectionLabel();
            }
        });

        JPanel north = new JPanel(new BorderLayout(8, 8));
        north.setOpaque(false);
        JLabel title = new JLabel("<html><span style='font-size:17px;color:#f1f5f9'><b>Intersection registry</b></span><br>"
            + "<span style='color:#94a3b8'>Every node in the 10×10 grid — sort columns, inspect queues.</span></html>");
        north.add(title, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(table);
        UiTheme.styleScroll(scroll);

        add(north, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(selectionDetail, BorderLayout.SOUTH);

        Timer refresh = new Timer(400, e -> refreshTable());
        refresh.start();
    }

    private void refreshTable() {
        List<Intersection> list = new ArrayList<>(controller.getIntersectionsView());
        list.sort(Comparator.comparing(Intersection::getIntersectionId));

        tableModel.setRowCount(0);
        for (Intersection i : list) {
            tableModel.addRow(new Object[]{
                i.getIntersectionId(),
                i.getX(),
                i.getY(),
                i.getCurrentSignal().name(),
                i.getQueueLength(),
                String.format("%.0f", i.getCongestionLevel() * 100.0),
                i.getGreenDuration(),
                i.getVehiclesProcessed()
            });
        }
        updateSelectionLabel();
    }

    private void updateSelectionLabel() {
        int row = table.getSelectedRow();
        if (row < 0) {
            selectionDetail.setText(" Select a row for a quick interpretation of that intersection.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = String.valueOf(tableModel.getValueAt(modelRow, 0));
        int q = Integer.parseInt(String.valueOf(tableModel.getValueAt(modelRow, 4)));
        double cong = Double.parseDouble(String.valueOf(tableModel.getValueAt(modelRow, 5))) / 100.0;
        String sig = String.valueOf(tableModel.getValueAt(modelRow, 3));
        selectionDetail.setText(String.format(
            " %s — signal %s, queue %d, congestion ~%.0f%%. Used by adaptive timing and corridor green waves.",
            id, sig, q, cong * 100.0
        ));
    }
}
