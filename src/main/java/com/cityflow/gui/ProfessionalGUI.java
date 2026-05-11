package com.cityflow.gui;

import com.cityflow.model.Intersection;
import com.cityflow.model.TrafficSignal;
import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Professional-grade realistic GUI for CityFlow simulation.
 * High-quality visualization with detailed statistics and analytics.
 */
public class ProfessionalGUI extends JFrame {
    private final List<Intersection> intersections;
    private final CentralController controller;
    private final CityMapPanel mapPanel;
    private final DetailedStatsPanel statsPanel;
    private final LiveChartsPanel chartsPanel;
    private javax.swing.Timer animationTimer;
    private List<AnimatedVehicle> vehicles;
    private Random random;
    private long startTime;
    
    // Data tracking
    private List<Double> congestionHistory = new ArrayList<>();
    private List<Integer> vehicleHistory = new ArrayList<>();
    private int totalVehiclesSpawned = 0;
    
    private static final int CELL_SIZE = 80;
    private static final int GRID_SIZE = 10;
    
    public ProfessionalGUI(List<Intersection> intersections, CentralController controller) {
        this.intersections = intersections;
        this.controller = controller;
        this.vehicles = new ArrayList<>();
        this.random = new Random();
        this.startTime = System.currentTimeMillis();
        
        setTitle("CityFlow - Professional Traffic Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use BorderLayout for main frame
        setLayout(new BorderLayout(0, 0));
        
        // Create header
        add(createProfessionalHeader(), BorderLayout.NORTH);
        
        // Create main content area
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Center: City map
        mapPanel = new CityMapPanel();
        JScrollPane mapScroll = new JScrollPane(mapPanel);
        mapScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 3),
                " 🗺️  LIVE CITY TRAFFIC MAP ",
                0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(mapScroll, BorderLayout.CENTER);
        
        // Right: Statistics panel
        statsPanel = new DetailedStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.EAST);
        
        // Bottom: Charts panel
        chartsPanel = new LiveChartsPanel();
        mainPanel.add(chartsPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize vehicles
        spawnInitialVehicles();
        
        // Set size and show
        setSize(1600, 950);
        setLocationRelativeTo(null);
        
        // Start animation
        startProfessionalAnimation();
    }
    
    private JPanel createProfessionalHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 40, 60));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Left side - Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel icon = new JLabel("🚦");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        leftPanel.add(icon);
        
        JLabel title = new JLabel("CityFlow Traffic Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        leftPanel.add(title);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Right side - Status indicators
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel liveLabel = new JLabel("● LIVE");
        liveLabel.setForeground(new Color(50, 255, 50));
        liveLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rightPanel.add(liveLabel);
        
        JLabel separator1 = new JLabel("|");
        separator1.setForeground(new Color(150, 150, 150));
        separator1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightPanel.add(separator1);
        
        JLabel intersectionLabel = new JLabel("100 Intersections");
        intersectionLabel.setForeground(new Color(200, 200, 255));
        intersectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rightPanel.add(intersectionLabel);
        
        JLabel separator2 = new JLabel("|");
        separator2.setForeground(new Color(150, 150, 150));
        separator2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightPanel.add(separator2);
        
        JLabel analyticsLabel = new JLabel("Real-time Analytics");
        analyticsLabel.setForeground(new Color(200, 200, 255));
        analyticsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rightPanel.add(analyticsLabel);
        
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private void spawnInitialVehicles() {
        // Spawn 50 vehicles
        for (int i = 0; i < 50; i++) {
            vehicles.add(new AnimatedVehicle());
            totalVehiclesSpawned++;
        }
    }
    
    private void startProfessionalAnimation() {
        animationTimer = new javax.swing.Timer(16, e -> {
            // Update vehicles
            for (AnimatedVehicle v : vehicles) {
                v.update();
            }
            
            // Occasionally spawn new vehicles
            if (random.nextInt(60) == 0 && vehicles.size() < 60) {
                vehicles.add(new AnimatedVehicle());
                totalVehiclesSpawned++;
            }
            
            // Remove vehicles that completed their journey
            vehicles.removeIf(v -> !v.isActive());
            
            // Update displays
            mapPanel.repaint();
            statsPanel.updateStatistics();
            chartsPanel.updateCharts();
        });
        animationTimer.start();
    }
    
    /**
     * Professional city map panel with high-quality rendering
     */
    private class CityMapPanel extends JPanel {
        public CityMapPanel() {
            setBackground(new Color(235, 240, 245));
            setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 100, GRID_SIZE * CELL_SIZE + 100));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Enable high-quality rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            int offsetX = 50;
            int offsetY = 50;
            
            // Draw professional road network
            drawProfessionalRoads(g2d, offsetX, offsetY);
            
            // Draw intersections with signals
            for (Intersection intersection : intersections) {
                drawProfessionalIntersection(g2d, intersection, offsetX, offsetY);
            }
            
            // Draw vehicles on top
            for (AnimatedVehicle vehicle : vehicles) {
                drawRealisticVehicle(g2d, vehicle, offsetX, offsetY);
            }
            
            // Draw legend
            drawLegend(g2d);
        }
        
        private void drawProfessionalRoads(Graphics2D g2d, int offsetX, int offsetY) {
            // Road base color
            g2d.setColor(new Color(60, 60, 70));
            
            // Draw thick horizontal roads
            g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i <= GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
            }
            
            // Draw thick vertical roads
            for (int i = 0; i <= GRID_SIZE; i++) {
                int x = offsetX + i * CELL_SIZE;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
            
            // Draw lane markings
            g2d.setColor(new Color(255, 220, 60));
            float[] dash = {10f, 10f};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash, 0f));
            
            // Horizontal lane markings
            for (int i = 0; i < GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
            }
            
            // Vertical lane markings
            for (int i = 0; i < GRID_SIZE; i++) {
                int x = offsetX + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
        }
        
        private void drawProfessionalIntersection(Graphics2D g2d, Intersection intersection, int offsetX, int offsetY) {
            int x = offsetX + intersection.getX() * CELL_SIZE;
            int y = offsetY + intersection.getY() * CELL_SIZE;
            int centerX = x + CELL_SIZE / 2;
            int centerY = y + CELL_SIZE / 2;
            
            // Draw congestion heatmap background
            double congestion = intersection.getCongestionLevel();
            Color heatColor = getDetailedHeatmapColor(congestion);
            g2d.setColor(new Color(heatColor.getRed(), heatColor.getGreen(), heatColor.getBlue(), 80));
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            
            // Draw intersection platform
            g2d.setColor(new Color(140, 140, 150));
            g2d.fillRoundRect(centerX - 15, centerY - 15, 30, 30, 5, 5);
            
            // Draw professional traffic light
            drawProfessionalTrafficLight(g2d, intersection, centerX, centerY);
            
            // Draw queue indicator with better design
            int queue = intersection.getQueueLength();
            if (queue > 0) {
                // Background circle
                g2d.setColor(new Color(220, 50, 50, 200));
                g2d.fillOval(x + 8, y + 8, 28, 28);
                
                // White border
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x + 8, y + 8, 28, 28);
                
                // Queue number
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String qText = String.valueOf(Math.min(queue, 99));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(qText);
                g2d.drawString(qText, x + 22 - textWidth/2, y + 26);
            }
            
            // Draw intersection ID
            g2d.setColor(new Color(80, 80, 100));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String id = "[" + intersection.getX() + "," + intersection.getY() + "]";
            g2d.drawString(id, centerX - 12, y + CELL_SIZE - 8);
        }
        
        private void drawProfessionalTrafficLight(Graphics2D g2d, Intersection intersection, int x, int y) {
            TrafficSignal signal = intersection.getCurrentSignal();
            
            // Traffic light housing
            g2d.setColor(new Color(30, 30, 40));
            g2d.fillRoundRect(x - 8, y - 20, 16, 40, 6, 6);
            
            // Border
            g2d.setColor(new Color(100, 100, 120));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x - 8, y - 20, 16, 40, 6, 6);
            
            int lightSize = 10;
            
            // RED light (top)
            drawTrafficLightBulb(g2d, x, y - 12, lightSize, 
                signal == TrafficSignal.RED, new Color(255, 40, 40), new Color(80, 20, 20));
            
            // YELLOW light (middle)
            drawTrafficLightBulb(g2d, x, y, lightSize,
                signal == TrafficSignal.YELLOW, new Color(255, 220, 0), new Color(80, 70, 0));
            
            // GREEN light (bottom)
            drawTrafficLightBulb(g2d, x, y + 12, lightSize,
                signal == TrafficSignal.GREEN, new Color(50, 255, 50), new Color(20, 80, 20));
        }
        
        private void drawTrafficLightBulb(Graphics2D g2d, int x, int y, int size, boolean active, Color onColor, Color offColor) {
            if (active) {
                // Glow effect
                for (int i = 3; i > 0; i--) {
                    g2d.setColor(new Color(onColor.getRed(), onColor.getGreen(), onColor.getBlue(), 30 * i));
                    g2d.fillOval(x - size/2 - i*2, y - size/2 - i*2, size + i*4, size + i*4);
                }
                g2d.setColor(onColor);
            } else {
                g2d.setColor(offColor);
            }
            
            g2d.fillOval(x - size/2, y - size/2, size, size);
            
            // Shine effect
            if (active) {
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillOval(x - size/2 + 2, y - size/2 + 2, size/3, size/3);
            }
        }
        
        private void drawRealisticVehicle(Graphics2D g2d, AnimatedVehicle vehicle, int offsetX, int offsetY) {
            int vx = offsetX + (int) vehicle.x;
            int vy = offsetY + (int) vehicle.y;
            
            g2d.rotate(vehicle.direction, vx, vy);
            
            // Car shadow
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillRoundRect(vx - 9, vy - 6, 20, 14, 4, 4);
            
            // Car body
            g2d.setColor(vehicle.color);
            g2d.fillRoundRect(vx - 10, vy - 7, 20, 14, 5, 5);
            
            // Car windows
            g2d.setColor(new Color(100, 150, 200, 180));
            g2d.fillRect(vx - 6, vy - 4, 5, 8);
            g2d.fillRect(vx + 2, vy - 4, 5, 8);
            
            // Headlights
            g2d.setColor(new Color(255, 255, 200));
            g2d.fillOval(vx + 8, vy - 5, 3, 3);
            g2d.fillOval(vx + 8, vy + 2, 3, 3);
            
            // Car outline
            g2d.setColor(vehicle.color.darker());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(vx - 10, vy - 7, 20, 14, 5, 5);
            
            g2d.rotate(-vehicle.direction, vx, vy);
        }
        
        private Color getDetailedHeatmapColor(double congestion) {
            if (congestion < 0.2) {
                return new Color(100, 230, 100);
            } else if (congestion < 0.4) {
                return new Color(180, 230, 100);
            } else if (congestion < 0.6) {
                return new Color(255, 200, 60);
            } else if (congestion < 0.8) {
                return new Color(255, 150, 60);
            } else {
                return new Color(255, 80, 80);
            }
        }
        
        private void drawLegend(Graphics2D g2d) {
            int lx = 20;
            int ly = getHeight() - 120;
            
            g2d.setColor(new Color(255, 255, 255, 230));
            g2d.fillRoundRect(lx, ly, 160, 100, 10, 10);
            
            g2d.setColor(new Color(100, 100, 120));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(lx, ly, 160, 100, 10, 10);
            
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.setColor(new Color(50, 50, 70));
            g2d.drawString("LEGEND", lx + 10, ly + 20);
            
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            
            // Green
            g2d.setColor(new Color(100, 230, 100));
            g2d.fillRect(lx + 10, ly + 30, 15, 15);
            g2d.setColor(new Color(50, 50, 70));
            g2d.drawString("Low Traffic", lx + 30, ly + 42);
            
            // Yellow
            g2d.setColor(new Color(255, 200, 60));
            g2d.fillRect(lx + 10, ly + 50, 15, 15);
            g2d.setColor(new Color(50, 50, 70));
            g2d.drawString("Moderate", lx + 30, ly + 62);
            
            // Red
            g2d.setColor(new Color(255, 80, 80));
            g2d.fillRect(lx + 10, ly + 70, 15, 15);
            g2d.setColor(new Color(50, 50, 70));
            g2d.drawString("High Traffic", lx + 30, ly + 82);
        }
    }
    
    /**
     * Detailed statistics panel with all metrics
     */
    private class DetailedStatsPanel extends JPanel {
        private JLabel vehiclesLabel, congestionLabel, throughputLabel, accuracyLabel;
        private JLabel uptimeLabel, activeVehiclesLabel, avgWaitLabel;
        private JTextArea bottleneckArea;
        
        public DetailedStatsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(250, 252, 255));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            setPreferredSize(new Dimension(340, 700));
            
            // Title
            JLabel title = new JLabel("📊 SYSTEM DASHBOARD");
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(new Color(70, 130, 180));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(title);
            add(Box.createVerticalStrut(20));
            
            // Primary metrics
            add(createSectionLabel("PRIMARY METRICS"));
            vehiclesLabel = createMetricLabel("Total Vehicles", "0", new Color(46, 134, 171));
            add(vehiclesLabel);
            add(Box.createVerticalStrut(12));
            
            activeVehiclesLabel = createMetricLabel("Active Vehicles", "0", new Color(6, 167, 125));
            add(activeVehiclesLabel);
            add(Box.createVerticalStrut(12));
            
            congestionLabel = createMetricLabel("Avg Congestion", "0%", new Color(230, 111, 81));
            add(congestionLabel);
            add(Box.createVerticalStrut(12));
            
            throughputLabel = createMetricLabel("Throughput", "0/min", new Color(46, 134, 171));
            add(throughputLabel);
            
            add(Box.createVerticalStrut(25));
            
            // Secondary metrics
            add(createSectionLabel("PERFORMANCE"));
            avgWaitLabel = createMetricLabel("Avg Wait Time", "0s", new Color(120, 120, 180));
            add(avgWaitLabel);
            add(Box.createVerticalStrut(12));
            
            accuracyLabel = createMetricLabel("Prediction Accuracy", "0%", new Color(6, 167, 125));
            add(accuracyLabel);
            add(Box.createVerticalStrut(12));
            
            uptimeLabel = createMetricLabel("System Uptime", "0s", new Color(100, 100, 120));
            add(uptimeLabel);
            
            add(Box.createVerticalStrut(25));
            
            // Bottlenecks
            add(createSectionLabel("🚨 BOTTLENECK ALERTS"));
            add(Box.createVerticalStrut(10));
            
            bottleneckArea = new JTextArea(8, 25);
            bottleneckArea.setEditable(false);
            bottleneckArea.setFont(new Font("Consolas", Font.PLAIN, 11));
            bottleneckArea.setBackground(new Color(255, 250, 240));
            bottleneckArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            JScrollPane scroll = new JScrollPane(bottleneckArea);
            scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(scroll);
        }
        
        private JLabel createSectionLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(new Color(80, 80, 100));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panel.setOpaque(false);
            panel.add(label);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            return label;
        }
        
        private JLabel createMetricLabel(String name, String value, Color valueColor) {
            JLabel label = new JLabel(
                "<html><div style='width:280px'><b>" + name + ":</b><br/>" +
                "<span style='font-size:20px; color:rgb(" + valueColor.getRed() + "," + 
                valueColor.getGreen() + "," + valueColor.getBlue() + ")'>" + 
                value + "</span></div></html>"
            );
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            return label;
        }
        
        public void updateStatistics() {
            Map<String, Object> stats = controller.getStatistics();
            
            int totalVehicles = totalVehiclesSpawned;
            int activeVehicles = vehicles.size();
            double congestion = (double) stats.get("averageCongestion");
            double accuracy = (double) stats.get("predictionAccuracy");
            
            long uptime = (System.currentTimeMillis() - startTime) / 1000;
            int throughput = (int) (totalVehicles / Math.max(1, uptime / 60.0));
            
            // Update labels
            vehiclesLabel.setText(
                "<html><div style='width:280px'><b>Total Vehicles:</b><br/>" +
                "<span style='font-size:20px; color:rgb(46,134,171)'>" + totalVehicles + "</span></div></html>"
            );
            
            activeVehiclesLabel.setText(
                "<html><div style='width:280px'><b>Active Vehicles:</b><br/>" +
                "<span style='font-size:20px; color:rgb(6,167,125)'>" + activeVehicles + "</span></div></html>"
            );
            
            Color congColor = congestion > 0.6 ? new Color(230, 57, 70) : new Color(6, 167, 125);
            congestionLabel.setText(
                "<html><div style='width:280px'><b>Avg Congestion:</b><br/>" +
                "<span style='font-size:20px; color:rgb(" + congColor.getRed() + "," + 
                congColor.getGreen() + "," + congColor.getBlue() + ")'>" + 
                String.format("%.1f%%", congestion * 100) + "</span></div></html>"
            );
            
            throughputLabel.setText(
                "<html><div style='width:280px'><b>Throughput:</b><br/>" +
                "<span style='font-size:20px; color:rgb(46,134,171)'>" + throughput + "/min</span></div></html>"
            );
            
            avgWaitLabel.setText(
                "<html><div style='width:280px'><b>Avg Wait Time:</b><br/>" +
                "<span style='font-size:20px; color:rgb(120,120,180)'>" + 
                String.format("%.1fs", 2.5 + congestion * 5) + "</span></div></html>"
            );
            
            accuracyLabel.setText(
                "<html><div style='width:280px'><b>Prediction Accuracy:</b><br/>" +
                "<span style='font-size:20px; color:rgb(6,167,125)'>" + 
                String.format("%.1f%%", accuracy * 100) + "</span></div></html>"
            );
            
            uptimeLabel.setText(
                "<html><div style='width:280px'><b>System Uptime:</b><br/>" +
                "<span style='font-size:20px; color:rgb(100,100,120)'>" + 
                String.format("%02d:%02d", uptime / 60, uptime % 60) + "</span></div></html>"
            );
            
            // Update bottlenecks
            Map<String, String> bottlenecks = controller.getBottlenecks();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, String> entry : bottlenecks.entrySet()) {
                if (count++ < 4) {
                    sb.append("⚠ ").append(entry.getKey()).append("\n");
                    sb.append("  ").append(entry.getValue()).append("\n\n");
                }
            }
            if (bottlenecks.isEmpty()) {
                sb.append("✓ All intersections operating\n  within normal parameters.\n\n");
                sb.append("  Traffic flow: OPTIMAL\n");
                sb.append("  System status: HEALTHY");
            }
            bottleneckArea.setText(sb.toString());
        }
    }
    
    /**
     * Live charts panel with professional graphs
     */
    private class LiveChartsPanel extends JPanel {
        public LiveChartsPanel() {
            setBackground(new Color(250, 252, 255));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 3),
                    " 📈 PERFORMANCE TRENDS (Last 2 Minutes) ",
                    0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            setPreferredSize(new Dimension(1000, 220));
        }
        
        public void updateCharts() {
            Map<String, Object> stats = controller.getStatistics();
            double congestion = (double) stats.get("averageCongestion");
            int vehicles = totalVehiclesSpawned;
            
            congestionHistory.add(congestion);
            vehicleHistory.add(vehicles);
            
            if (congestionHistory.size() > 120) {
                congestionHistory.remove(0);
                vehicleHistory.remove(0);
            }
            
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (congestionHistory.isEmpty()) return;
            
            int w = getWidth() - 80;
            int h = getHeight() - 80;
            int x0 = 50;
            int y0 = 40;
            
            // Draw axes
            g2d.setColor(new Color(150, 150, 170));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x0, y0 + h, x0 + w, y0 + h);
            g2d.drawLine(x0, y0, x0, y0 + h);
            
            // Draw grid
            g2d.setColor(new Color(220, 220, 230));
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i <= 4; i++) {
                int y = y0 + (h * i / 4);
                g2d.drawLine(x0, y, x0 + w, y);
            }
            
            // Draw congestion line
            if (congestionHistory.size() > 1) {
                g2d.setColor(new Color(230, 111, 81));
                g2d.setStroke(new BasicStroke(3));
                
                for (int i = 1; i < congestionHistory.size(); i++) {
                    int x1 = x0 + ((i - 1) * w / 120);
                    int y1 = y0 + h - (int) (congestionHistory.get(i - 1) * h);
                    int x2 = x0 + (i * w / 120);
                    int y2 = y0 + h - (int) (congestionHistory.get(i) * h);
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
            
            // Labels
            g2d.setColor(new Color(70, 70, 90));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.drawString("Congestion Level", x0 + w - 120, y0 - 10);
            g2d.drawString("Time →", x0 + w - 50, y0 + h + 25);
            
            // Y-axis labels
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2d.drawString("100%", x0 - 35, y0 + 5);
            g2d.drawString("50%", x0 - 30, y0 + h/2 + 5);
            g2d.drawString("0%", x0 - 20, y0 + h + 5);
        }
    }
    
    /**
     * Animated vehicle class
     */
    private class AnimatedVehicle {
        double x, y;
        double vx, vy;
        double direction;
        Color color;
        boolean active = true;
        int lifetime = 0;
        
        public AnimatedVehicle() {
            // Start at random intersection
            int gridX = random.nextInt(GRID_SIZE);
            int gridY = random.nextInt(GRID_SIZE);
            x = gridX * CELL_SIZE + CELL_SIZE / 2;
            y = gridY * CELL_SIZE + CELL_SIZE / 2;
            
            // Random direction (up, down, left, right)
            int dir = random.nextInt(4);
            switch (dir) {
                case 0: vx = 1.2; vy = 0; direction = 0; break;
                case 1: vx = -1.2; vy = 0; direction = Math.PI; break;
                case 2: vx = 0; vy = 1.2; direction = Math.PI/2; break;
                case 3: vx = 0; vy = -1.2; direction = -Math.PI/2; break;
            }
            
            // Random color
            Color[] colors = {
                new Color(231, 76, 60), new Color(52, 152, 219),
                new Color(46, 204, 113), new Color(241, 196, 15),
                new Color(155, 89, 182), new Color(230, 126, 34),
                new Color(26, 188, 156), new Color(52, 73, 94)
            };
            color = colors[random.nextInt(colors.length)];
        }
        
        public void update() {
            x += vx;
            y += vy;
            lifetime++;
            
            // Deactivate if out of bounds or too old
            if (x < -20 || x > GRID_SIZE * CELL_SIZE + 20 ||
                y < -20 || y > GRID_SIZE * CELL_SIZE + 20 ||
                lifetime > 800) {
                active = false;
            }
        }
        
        public boolean isActive() {
            return active;
        }
    }
    
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}
