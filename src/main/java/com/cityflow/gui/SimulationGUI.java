package com.cityflow.gui;

import com.cityflow.model.Intersection;
import com.cityflow.model.TrafficSignal;
import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.Map;

/**
 * Main GUI for CityFlow simulation.
 * Displays animated 10x10 grid with traffic signals and live metrics.
 */
public class SimulationGUI extends JFrame {
    private final List<Intersection> intersections;
    private final CentralController controller;
    private final GridPanel gridPanel;
    private final StatsPanel statsPanel;
    private Timer animationTimer;
    
    private static final int CELL_SIZE = 60;
    private static final int GRID_SIZE = 10;
    
    public SimulationGUI(List<Intersection> intersections, CentralController controller) {
        this.intersections = intersections;
        this.controller = controller;
        
        setTitle("CityFlow - Real-Time Traffic Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create grid panel
        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);
        
        // Create stats panel
        statsPanel = new StatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Set window size
        setSize(GRID_SIZE * CELL_SIZE + 50, GRID_SIZE * CELL_SIZE + 300);
        setLocationRelativeTo(null);
        
        // Start animation timer (60 FPS)
        startAnimation();
    }
    
    /**
     * Creates control panel with buttons
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("CityFlow Traffic Simulation");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * Starts animation timer
     */
    private void startAnimation() {
        animationTimer = new Timer(16, e -> { // ~60 FPS
            gridPanel.repaint();
            statsPanel.updateStats();
        });
        animationTimer.start();
    }
    
    /**
     * Panel that displays the traffic grid
     */
    private class GridPanel extends JPanel {
        public GridPanel() {
            setBackground(new Color(30, 30, 30));
            setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw grid lines
            g2d.setColor(new Color(60, 60, 60));
            for (int i = 0; i <= GRID_SIZE; i++) {
                g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
                g2d.drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
            }
            
            // Draw intersections
            for (Intersection intersection : intersections) {
                drawIntersection(g2d, intersection);
            }
        }
        
        /**
         * Draws a single intersection with signal and congestion indicator
         */
        private void drawIntersection(Graphics2D g2d, Intersection intersection) {
            int x = intersection.getX() * CELL_SIZE;
            int y = intersection.getY() * CELL_SIZE;
            
            // Draw congestion heatmap
            double congestion = intersection.getCongestionLevel();
            Color heatColor = getHeatmapColor(congestion);
            g2d.setColor(new Color(heatColor.getRed(), heatColor.getGreen(), 
                                   heatColor.getBlue(), 100));
            g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            
            // Draw traffic signal
            TrafficSignal signal = intersection.getCurrentSignal();
            Color signalColor = getSignalColor(signal);
            
            int centerX = x + CELL_SIZE / 2;
            int centerY = y + CELL_SIZE / 2;
            int signalSize = 20;
            
            // Draw signal with glow effect
            g2d.setColor(new Color(signalColor.getRed(), signalColor.getGreen(), 
                                   signalColor.getBlue(), 80));
            g2d.fillOval(centerX - signalSize, centerY - signalSize, 
                        signalSize * 2, signalSize * 2);
            
            g2d.setColor(signalColor);
            g2d.fillOval(centerX - signalSize/2, centerY - signalSize/2, 
                        signalSize, signalSize);
            
            // Draw queue length indicator
            int queueLength = intersection.getQueueLength();
            if (queueLength > 0) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String queueText = String.valueOf(queueLength);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(queueText);
                g2d.drawString(queueText, centerX - textWidth/2, y + 12);
            }
        }
        
        /**
         * Returns color based on congestion level (green to red)
         */
        private Color getHeatmapColor(double congestion) {
            if (congestion < 0.3) {
                return new Color(0, 200, 0); // Green
            } else if (congestion < 0.6) {
                return new Color(255, 200, 0); // Yellow
            } else {
                return new Color(255, 50, 50); // Red
            }
        }
        
        /**
         * Returns color for traffic signal
         */
        private Color getSignalColor(TrafficSignal signal) {
            switch (signal) {
                case RED:
                    return new Color(255, 0, 0);
                case YELLOW:
                    return new Color(255, 200, 0);
                case GREEN:
                    return new Color(0, 255, 0);
                default:
                    return Color.GRAY;
            }
        }
    }
    
    /**
     * Panel that displays live statistics
     */
    private class StatsPanel extends JPanel {
        private JLabel vehiclesLabel;
        private JLabel congestionLabel;
        private JLabel queueLabel;
        private JLabel accuracyLabel;
        
        public StatsPanel() {
            setBackground(new Color(40, 40, 40));
            setLayout(new GridLayout(2, 2, 20, 10));
            setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            vehiclesLabel = createStatLabel("Vehicles Processed: 0");
            congestionLabel = createStatLabel("Avg Congestion: 0.00");
            queueLabel = createStatLabel("Total Queue: 0");
            accuracyLabel = createStatLabel("Prediction Accuracy: 0.00%");
            
            add(vehiclesLabel);
            add(congestionLabel);
            add(queueLabel);
            add(accuracyLabel);
        }
        
        private JLabel createStatLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            return label;
        }
        
        /**
         * Updates statistics display
         */
        public void updateStats() {
            Map<String, Object> stats = controller.getStatistics();
            
            vehiclesLabel.setText("Vehicles Processed: " + stats.get("totalVehiclesProcessed"));
            congestionLabel.setText(String.format("Avg Congestion: %.2f", 
                stats.get("averageCongestion")));
            queueLabel.setText("Total Queue: " + stats.get("totalQueueLength"));
            accuracyLabel.setText(String.format("Prediction Accuracy: %.1f%%", 
                (double) stats.get("predictionAccuracy") * 100));
        }
    }
    
    /**
     * Stops animation when window is closed
     */
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}
