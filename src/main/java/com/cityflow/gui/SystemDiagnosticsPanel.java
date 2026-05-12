package com.cityflow.gui;

import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.time.Instant;
import java.util.Map;

/**
 * JVM footprint, controller aggregates, and remote client hints.
 */
public class SystemDiagnosticsPanel extends JPanel {
    private final int serverPort;
    private final CentralController controller;
    private final JTextArea text;

    public SystemDiagnosticsPanel(int serverPort, CentralController controller) {
        this.serverPort = serverPort;
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        setBackground(UiTheme.BG_DEEP);

        JLabel title = new JLabel("<html><span style='font-size:17px;color:#f1f5f9'><b>System diagnostics</b></span><br>"
            + "<span style='color:#94a3b8'>Process health, heap, threads, and how to attach a socket client.</span></html>");
        title.setFont(UiTheme.fontUi(Font.PLAIN, 15));
        add(title, BorderLayout.NORTH);

        text = new JTextArea();
        text.setEditable(false);
        text.setFont(UiTheme.fontMono(Font.PLAIN, 13));
        text.setMargin(new Insets(12, 14, 12, 14));
        text.setBackground(UiTheme.BG_CARD_INSET);
        text.setForeground(UiTheme.TEXT);
        text.setCaretColor(UiTheme.ACCENT);
        JScrollPane sp = new JScrollPane(text);
        UiTheme.styleScroll(sp);
        add(sp, BorderLayout.CENTER);

        Timer t = new Timer(900, e -> refresh());
        t.start();
    }

    private void refresh() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        long heapUsed = mem.getHeapMemoryUsage().getUsed();
        long heapMax = mem.getHeapMemoryUsage().getMax();
        Runtime rt = Runtime.getRuntime();

        Map<String, Object> stats = controller.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp: ").append(Instant.now()).append("\n\n");
        sb.append("Simulation socket server\n");
        sb.append("  Listen port: ").append(serverPort).append("\n");
        sb.append("  Sample client:\n");
        sb.append("  java -cp \"build:lib/*\" com.cityflow.network.Client localhost ")
            .append(serverPort).append("\n\n");

        sb.append("Java runtime\n");
        sb.append("  Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("  Available processors: ").append(rt.availableProcessors()).append("\n");
        sb.append("  Live threads (approx): ").append(ManagementFactory.getThreadMXBean().getThreadCount()).append("\n");
        sb.append(String.format("  Heap used: %,d / max %,d bytes%n", heapUsed, heapMax));

        sb.append("\nController snapshot\n");
        sb.append("  Active intersections: ").append(stats.get("activeIntersections")).append('\n');
        sb.append("  Total queue vehicles: ").append(stats.get("totalQueueLength")).append('\n');
        sb.append("  Vehicles processed (cumulative): ").append(stats.get("totalVehiclesProcessed")).append('\n');
        sb.append(String.format("  Average congestion: %.2f%%%n", ((Double) stats.get("averageCongestion")) * 100.0));

        text.setText(sb.toString());
        text.setCaretPosition(0);
    }
}
