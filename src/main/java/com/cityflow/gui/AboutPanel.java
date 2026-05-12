package com.cityflow.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Static project overview with hero-style presentation.
 */
public class AboutPanel extends JPanel {
    public AboutPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x1e293b), w, h, new Color(0x0f172a));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 16, 16);
                g2.setPaint(new GradientPaint(0, 0, new Color(77, 171, 247, 90), w, 0, new Color(151, 117, 250, 70)));
                g2.fillRoundRect(0, 0, w, 4, 16, 16);
                g2.dispose();
            }
        };
        hero.setOpaque(false);
        hero.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel headline = new JLabel("<html><div style='width:520px'>"
            + "<span style='font-size:26px;font-weight:700;color:#f8fafc'>CityFlow</span><br>"
            + "<span style='font-size:15px;color:#94a3b8'>Traffic operations suite · CS6103 · NYU Tandon</span></div></html>");
        hero.add(headline, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setOpaque(false);
        area.setForeground(new Color(0xe2e8f0));
        area.setFont(UiTheme.fontUi(Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(20, 0, 0, 0));
        area.setText("""
            What you are running

            A multi-threaded 10×10 traffic simulation with adaptive signals, predictive analytics,
            socket telemetry, and this tabbed desktop shell.

            Tabs

            Operations — live map, KPI stack, rolling congestion chart.
            Registry — sortable matrix of every intersection.
            Corridors — row/column health aligned with green-wave logic.
            Diagnostics — JVM and controller snapshot.
            Scenarios — spawn tuning, bursts, pause, synthetic bias.
            About — this page.

            MIT License — CityFlow team.
            """);

        hero.add(area, BorderLayout.CENTER);
        add(hero, BorderLayout.CENTER);
    }
}
