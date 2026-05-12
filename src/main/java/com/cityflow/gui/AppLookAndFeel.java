package com.cityflow.gui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * Installs FlatLaf dark theme and global UI defaults for CityFlow.
 */
public final class AppLookAndFeel {
    private AppLookAndFeel() {
    }

    public static void install() {
        boolean mac = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("mac");
        if (!mac) {
            System.setProperty("flatlaf.useWindowDecorations", "true");
        }

        try {
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("TabbedPane.tabHeight", 38);
            UIManager.put("TabbedPane.tabsOpaque", false);
            UIManager.put("TabbedPane.tabsBackground", UiTheme.BG_APP);
            UIManager.put("TabbedPane.background", UiTheme.BG_DEEP);
            UIManager.put("TabbedPane.foreground", UiTheme.TEXT);
            UIManager.put("TabbedPane.selectedBackground", UiTheme.BG_CARD);
            UIManager.put("TabbedPane.selectedForeground", UiTheme.TEXT);
            UIManager.put("TabbedPane.hoverColor", new Color(0x243044));
            UIManager.put("TabbedPane.underlineColor", UiTheme.ACCENT);
            UIManager.put("TabbedPane.contentAreaColor", UiTheme.BG_DEEP);
            UIManager.put("Table.selectionBackground", new Color(0x2f4a6f));
            UIManager.put("Table.selectionForeground", UiTheme.TEXT);
            UIManager.put("Table.gridColor", UiTheme.BORDER_SOFT);
            UIManager.put("Table.background", UiTheme.BG_CARD_INSET);
            UIManager.put("Table.foreground", UiTheme.TEXT);
            UIManager.put("TableHeader.background", UiTheme.BG_ELEVATED);
            UIManager.put("TableHeader.foreground", UiTheme.TEXT_MUTED);
            UIManager.put("TableHeader.separatorColor", UiTheme.BORDER_SOFT);

            FlatDarkLaf.setup();
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("FlatLaf not available, using system look and feel: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }
}
