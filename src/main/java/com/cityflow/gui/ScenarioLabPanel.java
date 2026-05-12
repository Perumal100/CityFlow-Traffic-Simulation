package com.cityflow.gui;

import com.cityflow.Main;
import com.cityflow.SimulationRuntime;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Experimental controls: spawn cadence, stress bursts, synthetic congestion bias.
 */
public class ScenarioLabPanel extends JPanel {
    public ScenarioLabPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        setBackground(UiTheme.BG_DEEP);

        JLabel intro = new JLabel("<html><span style='font-size:17px;color:#f1f5f9'><b>Scenario lab</b></span><br>"
            + "<span style='color:#94a3b8'>Tune spawn cadence, pause the spawner, inject bursts, or add synthetic congestion bias "
            + "for what-if analysis.</span></html>");
        intro.setFont(UiTheme.fontUi(Font.PLAIN, 14));
        add(intro, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        JPanel spawnPanel = new JPanel(new BorderLayout(8, 8));
        spawnPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
        spawnPanel.setBackground(UiTheme.BG_CARD);
        spawnPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                new TitledBorder(
                    BorderFactory.createEmptyBorder(),
                    "Vehicle spawn cadence (ms)",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    UiTheme.fontUi(Font.BOLD, 12),
                    UiTheme.ACCENT
                )
            ),
            BorderFactory.createEmptyBorder(8, 12, 12, 12)
        ));
        JSlider spawnSlider = new JSlider(200, 12000, Math.max(200, Math.min(12000, SimulationRuntime.getVehicleSpawnIntervalMs())));
        spawnSlider.setMajorTickSpacing(2000);
        spawnSlider.setMinorTickSpacing(400);
        spawnSlider.setPaintTicks(true);
        spawnSlider.setPaintLabels(true);
        JLabel spawnLabel = new JLabel(labelForMs(spawnSlider.getValue()));
        spawnLabel.setForeground(UiTheme.TEXT_MUTED);
        spawnSlider.addChangeListener(e -> {
            if (!spawnSlider.getValueIsAdjusting()) {
                SimulationRuntime.setVehicleSpawnIntervalMs(spawnSlider.getValue());
            }
            spawnLabel.setText(labelForMs(spawnSlider.getValue()));
        });
        spawnPanel.add(spawnSlider, BorderLayout.CENTER);
        spawnPanel.add(spawnLabel, BorderLayout.SOUTH);
        body.add(spawnPanel);
        body.add(Box.createVerticalStrut(14));

        JPanel biasPanel = new JPanel(new BorderLayout(8, 8));
        biasPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
        biasPanel.setBackground(UiTheme.BG_CARD);
        biasPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                new TitledBorder(
                    BorderFactory.createEmptyBorder(),
                    "Synthetic congestion bias",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    UiTheme.fontUi(Font.BOLD, 12),
                    UiTheme.ACCENT
                )
            ),
            BorderFactory.createEmptyBorder(8, 12, 12, 12)
        ));
        JSlider biasSlider = new JSlider(0, 35, (int) (SimulationRuntime.getCongestionBias() * 100));
        biasSlider.setMajorTickSpacing(10);
        biasSlider.setPaintTicks(true);
        biasSlider.setPaintLabels(true);
        JLabel biasLabel = new JLabel(String.format("Bias: +%d%%", biasSlider.getValue()));
        biasLabel.setForeground(UiTheme.TEXT_MUTED);
        biasSlider.addChangeListener(e -> {
            SimulationRuntime.setCongestionBias(biasSlider.getValue() / 100.0);
            biasLabel.setText(String.format("Bias: +%d%%", biasSlider.getValue()));
        });
        biasPanel.add(biasSlider, BorderLayout.CENTER);
        biasPanel.add(biasLabel, BorderLayout.SOUTH);
        body.add(biasPanel);
        body.add(Box.createVerticalStrut(14));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actions.setMaximumSize(new Dimension(Short.MAX_VALUE, 64));
        actions.setOpaque(false);
        JToggleButton pause = new JToggleButton("Pause spawning", SimulationRuntime.isSpawningPaused());
        pause.addActionListener(e -> SimulationRuntime.setSpawningPaused(pause.isSelected()));
        JButton burst10 = new JButton("Burst +10 vehicles");
        burst10.addActionListener(e -> Main.spawnVehicleBurst(10));
        JButton burst25 = new JButton("Burst +25 vehicles");
        burst25.addActionListener(e -> Main.spawnVehicleBurst(25));
        JButton burst80 = new JButton("Burst +80 vehicles");
        burst80.addActionListener(e -> Main.spawnVehicleBurst(80));
        actions.add(pause);
        actions.add(burst10);
        actions.add(burst25);
        actions.add(burst80);
        body.add(actions);

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(null);
        sp.getViewport().setBackground(UiTheme.BG_DEEP);
        add(sp, BorderLayout.CENTER);
    }

    private static String labelForMs(int ms) {
        return String.format("Current interval: %d ms (~%.1f spawns/min)", ms, 60000.0 / Math.max(1, ms));
    }
}
