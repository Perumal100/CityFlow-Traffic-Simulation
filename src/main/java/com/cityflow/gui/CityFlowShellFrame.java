package com.cityflow.gui;

import com.cityflow.controller.CentralController;
import com.cityflow.model.Intersection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Multi-page shell: hero chrome + tabbed navigation around the live operations dashboard.
 */
public class CityFlowShellFrame extends JFrame {
    private final OptimizedProfessionalGUI operationsDashboard;

    public CityFlowShellFrame(List<Intersection> intersections, CentralController controller, int serverPort) {
        setTitle("CityFlow — Traffic Operations Suite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(UiTheme.BG_DEEP);

        operationsDashboard = new OptimizedProfessionalGUI(intersections, controller);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.setFont(UiTheme.fontUi(Font.PLAIN, 13));
        tabs.setBackground(UiTheme.BG_DEEP);
        tabs.setForeground(UiTheme.TEXT);

        tabs.addTab("  Operations  ", operationsDashboard);
        tabs.addTab("  Registry  ", new IntersectionRegistryPanel(controller));
        tabs.addTab("  Corridors  ", new CorridorStrategyPanel(controller));
        tabs.addTab("  Diagnostics  ", new SystemDiagnosticsPanel(serverPort, controller));
        tabs.addTab("  Scenarios  ", new ScenarioLabPanel());
        tabs.addTab("  About  ", new AboutPanel());

        JPanel tabWrap = new JPanel(new BorderLayout());
        tabWrap.setOpaque(false);
        tabWrap.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
        tabWrap.add(tabs, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(new ShellHeroBar(), BorderLayout.NORTH);
        add(tabWrap, BorderLayout.CENTER);

        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.BORDER_SOFT),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        status.setBackground(UiTheme.BG_ELEVATED);
        JLabel hint = new JLabel("Multi-tab suite · simulation keeps running while you explore data");
        hint.setFont(UiTheme.fontUi(Font.PLAIN, 12));
        hint.setForeground(UiTheme.TEXT_MUTED);
        status.add(hint, BorderLayout.WEST);
        add(status, BorderLayout.SOUTH);
    }

    public void shutdownEmbedded() {
        operationsDashboard.stopAnimation();
    }
}
