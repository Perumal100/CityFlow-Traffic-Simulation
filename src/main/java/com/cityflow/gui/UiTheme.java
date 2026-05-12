package com.cityflow.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * CityFlow visual design tokens — tuned for dark, high-contrast operations UI.
 */
public final class UiTheme {
    public static final Color BG_DEEP = new Color(0x0a0e14);
    public static final Color BG_APP = new Color(0x0f141c);
    public static final Color BG_ELEVATED = new Color(0x151d2a);
    public static final Color BG_CARD = new Color(0x1a2434);
    public static final Color BG_CARD_INSET = new Color(0x121a26);
    public static final Color BORDER_SOFT = new Color(0x2a3d52);
    public static final Color ACCENT = new Color(0x4dabf7);
    public static final Color ACCENT_GLOW = new Color(0x339af0);
    public static final Color ACCENT_VIOLET = new Color(0x9775fa);
    public static final Color TEXT = new Color(0xe7edf4);
    public static final Color TEXT_MUTED = new Color(0x8b9cb0);
    public static final Color SUCCESS = new Color(0x51cf66);
    public static final Color WARNING = new Color(0xffd43b);
    public static final Color DANGER = new Color(0xff6b6b);

    private UiTheme() {
    }

    public static Font fontUi(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static Font fontMono(int style, int size) {
        return new Font(Font.MONOSPACED, style, size);
    }

    public static void styleScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_SOFT, 1));
        scroll.getViewport().setBackground(BG_CARD_INSET);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getHorizontalScrollBar().setUnitIncrement(14);
    }

    public static TitledBorder titled(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_SOFT, 1),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            fontUi(Font.BOLD, 12),
            ACCENT
        );
    }
}
