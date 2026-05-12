package com.cityflow.gui;

import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Read-only corridor / green-wave context derived from the same heuristics as the central controller.
 */
public class CorridorStrategyPanel extends JPanel {
    private final CentralController controller;
    private final JTextArea textArea;

    public CorridorStrategyPanel(CentralController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        setBackground(UiTheme.BG_DEEP);

        JLabel head = new JLabel("<html><span style='font-size:17px;color:#f1f5f9'><b>Corridor strategy</b></span><br>"
            + "<span style='color:#94a3b8'>When row/column averages sit between 30% and 70%, the controller "
            + "enters a green-wave window and aligns green durations.</span></html>");
        head.setFont(UiTheme.fontUi(Font.PLAIN, 14));
        add(head, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(UiTheme.fontMono(Font.PLAIN, 13));
        textArea.setMargin(new Insets(12, 14, 12, 14));
        textArea.setBackground(UiTheme.BG_CARD_INSET);
        textArea.setForeground(UiTheme.TEXT);
        textArea.setCaretColor(UiTheme.ACCENT);
        JScrollPane sp = new JScrollPane(textArea);
        UiTheme.styleScroll(sp);
        add(sp, BorderLayout.CENTER);

        Timer t = new Timer(1000, e -> refresh());
        t.start();
    }

    private void refresh() {
        List<String> lines = controller.getCorridorHealthLines();
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        Map<String, Object> stats = controller.getStatistics();
        sb.append('\n');
        sb.append(String.format("Fleet: processed=%s  queues(total)=%s  avg congestion=%.1f%%%n",
            stats.get("totalVehiclesProcessed"),
            stats.get("totalQueueLength"),
            ((Double) stats.get("averageCongestion")) * 100.0));
        sb.append(String.format("Prediction accuracy (rolling): %.1f%%%n",
            ((Double) stats.get("predictionAccuracy")) * 100.0));
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
    }
}
