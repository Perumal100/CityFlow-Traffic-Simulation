package com.cityflow.gui;

import com.cityflow.model.Intersection;
import com.cityflow.model.TrafficSignal;
import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

/**
 * Enhanced realistic GUI for CityFlow simulation.
 * Features: Road network, moving vehicles, live statistics, and professional design.
 */
public class EnhancedSimulationGUI extends JFrame {
    private final List<Intersection> intersections;
    private final CentralController controller;
    private final TrafficGridPanel gridPanel;
    private final LiveStatsPanel statsPanel;
    private final ChartsPanel chartsPanel;
    private Timer animationTimer;
    private List<MovingVehicle> vehicles;
    private Random random;
    
    private static final int CELL_SIZE = 70;
    private static final int GRID_SIZE = 10;
    
    public EnhancedSimulationGUI(List<Intersection> intersections, CentralController controller) {
        this.intersections = intersections;
        this.controller = controller;
        this.vehicles = new ArrayList<>();
        this.random = new Random();
        
        setTitle("CityFlow - Real-Time Traffic Simulation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContainer.setBackground(new Color(240, 240, 245));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Center: Traffic grid
        gridPanel = new TrafficGridPanel();
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
            "City Traffic Network - Live View",
            0, 0, new Font("Arial", Font.BOLD, 14), new Color(50, 50, 70)
        ));
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Right: Live statistics
        statsPanel = new LiveStatsPanel();
        mainContainer.add(statsPanel, BorderLayout.EAST);
        
        // Bottom: Charts
        chartsPanel = new ChartsPanel();
        mainContainer.add(chartsPanel, BorderLayout.SOUTH);
        
        add(mainContainer);
        
        // Set window size
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        // Initialize vehicles
        initializeVehicles();
        
        // Start animation timer (60 FPS)
        startAnimation();
    }
    
    /**
     * Creates professional header panel
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 55, 75));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🚦 CityFlow Traffic Simulation System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel("● LIVE  |  100 Intersections  |  Real-time Analytics");
        statusLabel.setForeground(new Color(100, 255, 100));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Initializes moving vehicles
     */
    private void initializeVehicles() {
        // Create 30 vehicles moving around the grid
        for (int i = 0; i < 30; i++) {
            vehicles.add(new MovingVehicle());
        }
    }
    
    /**
     * Starts animation timer
     */
    private void startAnimation() {
        animationTimer = new Timer(16, e -> { // ~60 FPS
            // Update vehicle positions
            for (MovingVehicle vehicle : vehicles) {
                vehicle.update();
            }
            
            gridPanel.repaint();
            statsPanel.updateStats();
            chartsPanel.updateCharts();
        });
        animationTimer.start();
    }
    
    /**
     * Panel that displays the traffic grid with roads and vehicles
     */
    private class TrafficGridPanel extends JPanel {
        public TrafficGridPanel() {
            setBackground(new Color(220, 225, 230));
            setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 40, GRID_SIZE * CELL_SIZE + 40));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int offsetX = 20;
            int offsetY = 20;
            
            // Draw road network
            drawRoads(g2d, offsetX, offsetY);
            
            // Draw intersections
            for (Intersection intersection : intersections) {
                drawIntersection(g2d, intersection, offsetX, offsetY);
            }
            
            // Draw moving vehicles
            for (MovingVehicle vehicle : vehicles) {
                drawVehicle(g2d, vehicle, offsetX, offsetY);
            }
        }
        
        /**
         * Draws road network
         */
        private void drawRoads(Graphics2D g2d, int offsetX, int offsetY) {
            g2d.setColor(new Color(80, 80, 90));
            g2d.setStroke(new BasicStroke(3));
            
            // Horizontal roads
            for (int i = 0; i <= GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
            }
            
            // Vertical roads
            for (int i = 0; i <= GRID_SIZE; i++) {
                int x = offsetX + i * CELL_SIZE;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
            
            // Road markings (dashed lines)
            g2d.setColor(new Color(255, 255, 100));
            Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, new float[]{5}, 0);
            g2d.setStroke(dashed);
            
            for (int i = 0; i < GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
            }
            
            for (int i = 0; i < GRID_SIZE; i++) {
                int x = offsetX + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
        }
        
        /**
         * Draws a single intersection with traffic signal
         */
        private void drawIntersection(Graphics2D g2d, Intersection intersection, int offsetX, int offsetY) {
            int x = offsetX + intersection.getX() * CELL_SIZE;
            int y = offsetY + intersection.getY() * CELL_SIZE;
            
            // Draw congestion heatmap
            double congestion = intersection.getCongestionLevel();
            Color heatColor = getHeatmapColor(congestion);
            g2d.setColor(new Color(heatColor.getRed(), heatColor.getGreen(), 
                                   heatColor.getBlue(), 60));
            g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            
            // Draw intersection box
            g2d.setColor(new Color(160, 160, 170));
            g2d.fillRect(x + 25, y + 25, 20, 20);
            
            // Draw traffic signal (realistic 3-light design)
            drawTrafficLight(g2d, intersection, x + 35, y + 35);
            
            // Draw queue length indicator
            int queueLength = intersection.getQueueLength();
            if (queueLength > 0) {
                g2d.setColor(new Color(255, 100, 100));
                g2d.fillOval(x + 5, y + 5, 15, 15);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String queueText = String.valueOf(queueLength);
                g2d.drawString(queueText, x + 8, y + 15);
            }
            
            // Draw intersection ID
            g2d.setColor(new Color(100, 100, 120));
            g2d.setFont(new Font("Arial", Font.PLAIN, 8));
            g2d.drawString(intersection.getX() + "," + intersection.getY(), x + 28, y + 65);
        }
        
        /**
         * Draws a realistic traffic light
         */
        private void drawTrafficLight(Graphics2D g2d, Intersection intersection, int x, int y) {
            TrafficSignal signal = intersection.getCurrentSignal();
            
            // Draw traffic light casing
            g2d.setColor(new Color(40, 40, 50));
            g2d.fillRoundRect(x - 4, y - 12, 8, 24, 3, 3);
            
            // Three lights
            int lightSize = 6;
            
            // Red light
            if (signal == TrafficSignal.RED) {
                g2d.setColor(new Color(255, 50, 50));
                // Glow effect
                g2d.setColor(new Color(255, 0, 0, 100));
                g2d.fillOval(x - 5, y - 11, 10, 10);
                g2d.setColor(new Color(255, 50, 50));
            } else {
                g2d.setColor(new Color(80, 30, 30));
            }
            g2d.fillOval(x - 3, y - 9, lightSize, lightSize);
            
            // Yellow light
            if (signal == TrafficSignal.YELLOW) {
                g2d.setColor(new Color(255, 255, 0));
                // Glow effect
                g2d.setColor(new Color(255, 255, 0, 100));
                g2d.fillOval(x - 5, y - 3, 10, 10);
                g2d.setColor(new Color(255, 255, 0));
            } else {
                g2d.setColor(new Color(80, 80, 30));
            }
            g2d.fillOval(x - 3, y - 1, lightSize, lightSize);
            
            // Green light
            if (signal == TrafficSignal.GREEN) {
                g2d.setColor(new Color(50, 255, 50));
                // Glow effect
                g2d.setColor(new Color(0, 255, 0, 100));
                g2d.fillOval(x - 5, y + 5, 10, 10);
                g2d.setColor(new Color(50, 255, 50));
            } else {
                g2d.setColor(new Color(30, 80, 30));
            }
            g2d.fillOval(x - 3, y + 7, lightSize, lightSize);
        }
        
        /**
         * Draws a moving vehicle
         */
        private void drawVehicle(Graphics2D g2d, MovingVehicle vehicle, int offsetX, int offsetY) {
            int x = offsetX + (int) vehicle.x;
            int y = offsetY + (int) vehicle.y;
            
            // Car body
            g2d.setColor(vehicle.color);
            g2d.fillRoundRect(x - 6, y - 4, 12, 8, 3, 3);
            
            // Car windows
            g2d.setColor(new Color(100, 150, 200));
            g2d.fillRect(x - 4, y - 2, 3, 4);
            g2d.fillRect(x + 2, y - 2, 3, 4);
            
            // Headlights
            g2d.setColor(new Color(255, 255, 200));
            g2d.fillOval(x + 5, y - 3, 2, 2);
            g2d.fillOval(x + 5, y + 1, 2, 2);
        }
        
        /**
         * Returns color based on congestion level
         */
        private Color getHeatmapColor(double congestion) {
            if (congestion < 0.3) {
                return new Color(100, 220, 100); // Green
            } else if (congestion < 0.6) {
                return new Color(255, 200, 50); // Yellow
            } else {
                return new Color(255, 80, 80); // Red
            }
        }
    }
    
    /**
     * Panel displaying live statistics
     */
    private class LiveStatsPanel extends JPanel {
        private JLabel vehiclesLabel;
        private JLabel congestionLabel;
        private JLabel throughputLabel;
        private JLabel accuracyLabel;
        private JTextArea bottlenecksArea;
        
        public LiveStatsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(250, 250, 255));
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
                "Live Analytics Dashboard",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(50, 50, 70)
            ));
            setPreferredSize(new Dimension(280, 600));
            
            add(Box.createVerticalStrut(10));
            
            // Title
            JLabel title = new JLabel("📊 System Metrics");
            title.setFont(new Font("Arial", Font.BOLD, 16));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(title);
            
            add(Box.createVerticalStrut(15));
            
            // Statistics
            vehiclesLabel = createStatLabel("Vehicles Processed", "0");
            congestionLabel = createStatLabel("Avg Congestion", "0%");
            throughputLabel = createStatLabel("Throughput", "0/min");
            accuracyLabel = createStatLabel("Prediction Accuracy", "0%");
            
            add(vehiclesLabel);
            add(Box.createVerticalStrut(10));
            add(congestionLabel);
            add(Box.createVerticalStrut(10));
            add(throughputLabel);
            add(Box.createVerticalStrut(10));
            add(accuracyLabel);
            
            add(Box.createVerticalStrut(20));
            
            // Bottlenecks section
            JLabel bottleneckTitle = new JLabel("🚨 Bottleneck Alerts");
            bottleneckTitle.setFont(new Font("Arial", Font.BOLD, 14));
            bottleneckTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(bottleneckTitle);
            
            add(Box.createVerticalStrut(10));
            
            bottlenecksArea = new JTextArea(10, 20);
            bottlenecksArea.setEditable(false);
            bottlenecksArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            bottlenecksArea.setBackground(new Color(255, 250, 240));
            JScrollPane scrollPane = new JScrollPane(bottlenecksArea);
            scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(scrollPane);
        }
        
        private JLabel createStatLabel(String name, String value) {
            JLabel label = new JLabel(
                "<html><b>" + name + ":</b><br/>" +
                "<font size='+2' color='#2E86AB'>" + value + "</font></html>"
            );
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return label;
        }
        
        public void updateStats() {
            Map<String, Object> stats = controller.getStatistics();
            
            int vehicles = (int) stats.get("totalVehiclesProcessed");
            double congestion = (double) stats.get("averageCongestion");
            double accuracy = (double) stats.get("predictionAccuracy");
            
            vehiclesLabel.setText(
                "<html><b>Vehicles Processed:</b><br/>" +
                "<font size='+2' color='#2E86AB'>" + vehicles + "</font></html>"
            );
            
            congestionLabel.setText(
                "<html><b>Avg Congestion:</b><br/>" +
                "<font size='+2' color='" + (congestion > 0.6 ? "#E63946" : "#06A77D") + "'>" + 
                String.format("%.1f%%", congestion * 100) + "</font></html>"
            );
            
            int throughput = vehicles / Math.max(1, (int)(System.currentTimeMillis() / 60000));
            throughputLabel.setText(
                "<html><b>Throughput:</b><br/>" +
                "<font size='+2' color='#2E86AB'>" + throughput + "/min</font></html>"
            );
            
            accuracyLabel.setText(
                "<html><b>Prediction Accuracy:</b><br/>" +
                "<font size='+2' color='#06A77D'>" + 
                String.format("%.1f%%", accuracy * 100) + "</font></html>"
            );
            
            // Update bottlenecks
            Map<String, String> bottlenecks = controller.getBottlenecks();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, String> entry : bottlenecks.entrySet()) {
                if (count++ < 5) {
                    sb.append("⚠ [").append(entry.getKey()).append("]\n");
                    sb.append("  ").append(entry.getValue()).append("\n\n");
                }
            }
            if (bottlenecks.isEmpty()) {
                sb.append("✓ All intersections\n  operating normally");
            }
            bottlenecksArea.setText(sb.toString());
        }
    }
    
    /**
     * Panel displaying live charts
     */
    private class ChartsPanel extends JPanel {
        private List<Double> congestionHistory;
        private List<Integer> throughputHistory;
        
        public ChartsPanel() {
            setBackground(new Color(250, 250, 255));
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
                "Performance Trends (Last 60 seconds)",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(50, 50, 70)
            ));
            setPreferredSize(new Dimension(800, 180));
            
            congestionHistory = new ArrayList<>();
            throughputHistory = new ArrayList<>();
        }
        
        public void updateCharts() {
            Map<String, Object> stats = controller.getStatistics();
            double congestion = (double) stats.get("averageCongestion");
            int vehicles = (int) stats.get("totalVehiclesProcessed");
            
            congestionHistory.add(congestion);
            throughputHistory.add(vehicles);
            
            // Keep last 60 data points
            if (congestionHistory.size() > 60) {
                congestionHistory.remove(0);
                throughputHistory.remove(0);
            }
            
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (congestionHistory.isEmpty()) return;
            
            int width = getWidth() - 60;
            int height = getHeight() - 60;
            int x0 = 40;
            int y0 = 30;
            
            // Draw axes
            g2d.setColor(new Color(150, 150, 150));
            g2d.drawLine(x0, y0 + height, x0 + width, y0 + height); // X-axis
            g2d.drawLine(x0, y0, x0, y0 + height); // Y-axis
            
            // Draw congestion line chart
            g2d.setColor(new Color(231, 111, 81));
            g2d.setStroke(new BasicStroke(2));
            
            for (int i = 1; i < congestionHistory.size(); i++) {
                int x1 = x0 + (i - 1) * width / 60;
                int y1 = y0 + height - (int) (congestionHistory.get(i - 1) * height);
                int x2 = x0 + i * width / 60;
                int y2 = y0 + height - (int) (congestionHistory.get(i) * height);
                g2d.drawLine(x1, y1, x2, y2);
            }
            
            // Labels
            g2d.setColor(new Color(80, 80, 100));
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString("Congestion Level", x0 + width - 100, y0 - 5);
            g2d.drawString("Time →", x0 + width - 40, y0 + height + 20);
        }
    }
    
    /**
     * Class representing a moving vehicle
     */
    private class MovingVehicle {
        double x, y;
        double vx, vy;
        Color color;
        
        public MovingVehicle() {
            x = random.nextInt(GRID_SIZE * CELL_SIZE);
            y = random.nextInt(GRID_SIZE * CELL_SIZE);
            
            // Random velocity
            double speed = 0.5 + random.nextDouble() * 1.5;
            double angle = random.nextDouble() * Math.PI * 2;
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;
            
            // Random car color
            Color[] colors = {
                new Color(255, 100, 100),
                new Color(100, 100, 255),
                new Color(100, 255, 100),
                new Color(255, 255, 100),
                new Color(200, 100, 255)
            };
            color = colors[random.nextInt(colors.length)];
        }
        
        public void update() {
            x += vx;
            y += vy;
            
            // Bounce off boundaries
            if (x < 0 || x > GRID_SIZE * CELL_SIZE) vx = -vx;
            if (y < 0 || y > GRID_SIZE * CELL_SIZE) vy = -vy;
            
            // Keep in bounds
            x = Math.max(0, Math.min(GRID_SIZE * CELL_SIZE, x));
            y = Math.max(0, Math.min(GRID_SIZE * CELL_SIZE, y));
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
