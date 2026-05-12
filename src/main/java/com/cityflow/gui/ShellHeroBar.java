package com.cityflow.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Top chrome strip with gradient and brand copy.
 */
public class ShellHeroBar extends JPanel {
    public ShellHeroBar() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(10, 78));
        setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel brand = new JLabel(
            "<html><span style='font-size:24px;font-weight:700;color:#f1f5f9;letter-spacing:-0.5px'>CityFlow</span>"
                + "<br><span style='font-size:12px;color:#94a3b8'>Traffic intelligence · live grid · adaptive signals</span></html>"
        );

        JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        east.setOpaque(false);
        JLabel pill = new JLabel("  LIVE  ");
        pill.setOpaque(true);
        pill.setBackground(new Color(0x14532d));
        pill.setForeground(new Color(0x86efac));
        pill.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x22c55e), 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        pill.setFont(UiTheme.fontUi(Font.BOLD, 11));
        JLabel ver = new JLabel("Suite v2");
        ver.setForeground(UiTheme.TEXT_MUTED);
        ver.setFont(UiTheme.fontUi(Font.PLAIN, 12));
        east.add(pill);
        east.add(ver);

        add(brand, BorderLayout.WEST);
        add(east, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(0x1a2f4a),
            w, h, new Color(0x0a1624)
        );
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        g2.setPaint(new GradientPaint(0, 0, new Color(77, 171, 247, 90), w, 0, new Color(151, 117, 250, 70)));
        g2.fillRect(0, 0, w, 3);

        g2.setColor(new Color(255, 255, 255, 22));
        g2.drawLine(0, h - 1, w, h - 1);
        g2.dispose();
    }
}
