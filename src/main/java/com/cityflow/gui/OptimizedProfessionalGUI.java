package com.cityflow.gui;

import com.cityflow.model.Intersection;
import com.cityflow.model.TrafficSignal;
import com.cityflow.controller.CentralController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ultra-optimized professional GUI with smooth 60 FPS performance
 */
public class OptimizedProfessionalGUI extends JFrame {
    private final List<Intersection> intersections;
    private final CentralController controller;
    private final OptimizedMapPanel mapPanel;
    private final OptimizedStatsPanel statsPanel;
    private final OptimizedChartsPanel chartsPanel;
    private javax.swing.Timer animationTimer;
    private CopyOnWriteArrayList<Vehicle> vehicles;
    private Random random;
    private long startTime;
    
    // Performance optimization
    private int frameCount = 0;
    private int totalVehiclesSpawned = 0;
    
    private static final int CELL_SIZE = 75;
    private static final int GRID_SIZE = 10;
    private static final int TARGET_FPS = 60;
    
    public OptimizedProfessionalGUI(List<Intersection> intersections, CentralController controller) {
        this.intersections = intersections;
        this.controller = controller;
        this.vehicles = new CopyOnWriteArrayList<>();
        this.random = new Random();
        this.startTime = System.currentTimeMillis();
        
        setTitle("CityFlow - Professional Traffic Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Auto-maximize
        
        // Main layout
        setLayout(new BorderLayout(0, 0));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create panels
        mapPanel = new OptimizedMapPanel();
        statsPanel = new OptimizedStatsPanel();
        chartsPanel = new OptimizedChartsPanel();
        
        // Map (center)
        JScrollPane mapScroll = new JScrollPane(mapPanel);
        mapScroll.setBorder(createTitledBorder("🗺️  LIVE CITY TRAFFIC MAP"));
        mainPanel.add(mapScroll, BorderLayout.CENTER);
        
        // Stats (right)
        JScrollPane statsScroll = new JScrollPane(statsPanel);
        statsScroll.setBorder(createTitledBorder("📊  ANALYTICS DASHBOARD"));
        statsScroll.setPreferredSize(new Dimension(350, 600));
        mainPanel.add(statsScroll, BorderLayout.EAST);
        
        // Charts (bottom)
        chartsPanel.setBorder(createTitledBorder("📈  REAL-TIME PERFORMANCE METRICS"));
        chartsPanel.setPreferredSize(new Dimension(1000, 200));
        mainPanel.add(chartsPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Spawn initial vehicles
        spawnVehicles(40);
        
        // Start optimized animation
        startOptimizedAnimation();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 35, 50));
        header.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        
        // Left
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);
        
        JLabel icon = new JLabel("🚦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        left.add(icon);
        
        JLabel title = new JLabel("CityFlow Traffic Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        left.add(title);
        
        header.add(left, BorderLayout.WEST);
        
        // Right
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        right.setOpaque(false);
        
        JLabel live = new JLabel("● LIVE");
        live.setForeground(new Color(50, 255, 100));
        live.setFont(new Font("Segoe UI", Font.BOLD, 15));
        right.add(live);
        
        JLabel sep1 = new JLabel("|");
        sep1.setForeground(new Color(150, 150, 150));
        right.add(sep1);
        
        JLabel ints = new JLabel("100 Intersections");
        ints.setForeground(new Color(200, 220, 255));
        ints.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        right.add(ints);
        
        JLabel sep2 = new JLabel("|");
        sep2.setForeground(new Color(150, 150, 150));
        right.add(sep2);
        
        JLabel analytics = new JLabel("Real-time Analytics");
        analytics.setForeground(new Color(200, 220, 255));
        analytics.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        right.add(analytics);
        
        header.add(right, BorderLayout.EAST);
        
        return header;
    }
    
    private javax.swing.border.Border createTitledBorder(String title) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                title,
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
    
    private void spawnVehicles(int count) {
        for (int i = 0; i < count; i++) {
            vehicles.add(new Vehicle());
            totalVehiclesSpawned++;
        }
    }
    
    private void startOptimizedAnimation() {
        animationTimer = new javax.swing.Timer(1000 / TARGET_FPS, e -> {
            frameCount++;
            
            // Update vehicles
            for (Vehicle v : vehicles) {
                v.update();
            }
            
            // Spawn new vehicles occasionally
            if (frameCount % 90 == 0 && vehicles.size() < 50) {
                vehicles.add(new Vehicle());
                totalVehiclesSpawned++;
            }
            
            // Remove inactive vehicles
            vehicles.removeIf(v -> !v.active);
            
            // Update UI (optimized intervals)
            mapPanel.repaint();
            
            if (frameCount % 15 == 0) { // Update stats every 15 frames (4 times/sec)
                statsPanel.updateStats();
            }
            
            if (frameCount % 30 == 0) { // Update chart every 30 frames (2 times/sec)
                chartsPanel.updateChart();
            }
        });
        animationTimer.start();
    }
    
    /**
     * Optimized map panel with efficient rendering
     */
    private class OptimizedMapPanel extends JPanel {
        private BufferedImage roadLayer; // Cache road network
        
        public OptimizedMapPanel() {
            setBackground(new Color(230, 235, 240));
            setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 100, GRID_SIZE * CELL_SIZE + 100));
            setDoubleBuffered(true);
            
            // Pre-render roads
            createRoadLayer();
        }
        
        private void createRoadLayer() {
            int width = GRID_SIZE * CELL_SIZE + 100;
            int height = GRID_SIZE * CELL_SIZE + 100;
            roadLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roadLayer.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int offsetX = 50;
            int offsetY = 50;
            
            // Background
            g2d.setColor(new Color(230, 235, 240));
            g2d.fillRect(0, 0, width, height);
            
            // Roads
            g2d.setColor(new Color(55, 55, 65));
            g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            for (int i = 0; i <= GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
                
                int x = offsetX + i * CELL_SIZE;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
            
            // Lane marks
            g2d.setColor(new Color(255, 220, 60));
            float[] dash = {8f, 8f};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash, 0f));
            
            for (int i = 0; i < GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
                
                int x = offsetX + i * CELL_SIZE + CELL_SIZE / 2;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
            
            g2d.dispose();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            
            int offsetX = 50;
            int offsetY = 50;
            
            // Draw cached road layer
            g2d.drawImage(roadLayer, 0, 0, null);
            
            // Draw intersections
            for (Intersection intersection : intersections) {
                drawIntersection(g2d, intersection, offsetX, offsetY);
            }
            
            // Draw vehicles
            for (Vehicle vehicle : vehicles) {
                drawVehicle(g2d, vehicle, offsetX, offsetY);
            }
        }
        
        private void drawIntersection(Graphics2D g2d, Intersection intersection, int offsetX, int offsetY) {
            int x = offsetX + intersection.getX() * CELL_SIZE;
            int y = offsetY + intersection.getY() * CELL_SIZE;
            int cx = x + CELL_SIZE / 2;
            int cy = y + CELL_SIZE / 2;
            
            // Congestion heatmap
            double cong = intersection.getCongestionLevel();
            Color heat = cong < 0.3 ? new Color(100, 230, 100, 70) :
                        cong < 0.6 ? new Color(255, 200, 60, 70) :
                        new Color(255, 80, 80, 70);
            g2d.setColor(heat);
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            
            // Intersection platform
            g2d.setColor(new Color(130, 130, 140));
            g2d.fillRoundRect(cx - 12, cy - 12, 24, 24, 4, 4);
            
            // Traffic light
            drawTrafficLight(g2d, intersection.getCurrentSignal(), cx, cy);
            
            // Queue indicator
            int queue = intersection.getQueueLength();
            if (queue > 0) {
                g2d.setColor(new Color(255, 60, 60));
                g2d.fillOval(x + 10, y + 10, 22, 22);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 13));
                String qStr = queue > 99 ? "99+" : String.valueOf(queue);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(qStr, x + 21 - fm.stringWidth(qStr)/2, y + 24);
            }
        }
        
        private void drawTrafficLight(Graphics2D g2d, TrafficSignal signal, int x, int y) {
            // Housing
            g2d.setColor(new Color(25, 25, 35));
            g2d.fillRoundRect(x - 6, y - 16, 12, 32, 5, 5);
            
            int sz = 8;
            
            // Red
            if (signal == TrafficSignal.RED) {
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillOval(x - sz - 2, y - 10 - sz - 2, sz * 2 + 4, sz * 2 + 4);
                g2d.setColor(new Color(255, 50, 50));
            } else {
                g2d.setColor(new Color(70, 20, 20));
            }
            g2d.fillOval(x - sz/2, y - 10 - sz/2, sz, sz);
            
            // Yellow
            if (signal == TrafficSignal.YELLOW) {
                g2d.setColor(new Color(255, 255, 200, 80));
                g2d.fillOval(x - sz - 2, y - sz - 2, sz * 2 + 4, sz * 2 + 4);
                g2d.setColor(new Color(255, 220, 0));
            } else {
                g2d.setColor(new Color(70, 60, 0));
            }
            g2d.fillOval(x - sz/2, y - sz/2, sz, sz);
            
            // Green
            if (signal == TrafficSignal.GREEN) {
                g2d.setColor(new Color(200, 255, 200, 80));
                g2d.fillOval(x - sz - 2, y + 10 - sz - 2, sz * 2 + 4, sz * 2 + 4);
                g2d.setColor(new Color(50, 255, 50));
            } else {
                g2d.setColor(new Color(20, 70, 20));
            }
            g2d.fillOval(x - sz/2, y + 10 - sz/2, sz, sz);
        }
        
        private void drawVehicle(Graphics2D g2d, Vehicle v, int offsetX, int offsetY) {
            int vx = offsetX + (int) v.x;
            int vy = offsetY + (int) v.y;
            
            AffineTransform old = g2d.getTransform();
            g2d.rotate(v.angle, vx, vy);
            
            // Shadow
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRoundRect(vx - 8, vy - 5, 18, 12, 3, 3);
            
            // Body
            g2d.setColor(v.color);
            g2d.fillRoundRect(vx - 9, vy - 6, 18, 12, 4, 4);
            
            // Windows
            g2d.setColor(new Color(120, 160, 200, 180));
            g2d.fillRect(vx - 5, vy - 3, 4, 6);
            g2d.fillRect(vx + 2, vy - 3, 4, 6);
            
            // Headlights
            g2d.setColor(new Color(255, 255, 220));
            g2d.fillOval(vx + 7, vy - 4, 2, 2);
            g2d.fillOval(vx + 7, vy + 2, 2, 2);
            
            g2d.setTransform(old);
        }
    }
    
    /**
     * Optimized stats panel
     */
    private class OptimizedStatsPanel extends JPanel {
        private JLabel[] metricLabels = new JLabel[7];
        private JTextArea bottleneckArea;
        
        public OptimizedStatsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            
            add(Box.createVerticalStrut(10));
            
            JLabel title = new JLabel("LIVE METRICS");
            title.setFont(new Font("Segoe UI", Font.BOLD, 16));
            title.setForeground(new Color(50, 50, 70));
            title.setAlignmentX(LEFT_ALIGNMENT);
            add(title);
            
            add(Box.createVerticalStrut(15));
            
            String[] names = {
                "Total Vehicles", "Active Vehicles", "Avg Congestion",
                "Throughput", "Avg Wait Time", "Prediction Accuracy", "Uptime"
            };
            
            for (int i = 0; i < names.length; i++) {
                metricLabels[i] = createMetricLabel(names[i], "0");
                add(metricLabels[i]);
                add(Box.createVerticalStrut(10));
            }
            
            add(Box.createVerticalStrut(15));
            
            JLabel btTitle = new JLabel("🚨 ALERTS");
            btTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btTitle.setForeground(new Color(50, 50, 70));
            btTitle.setAlignmentX(LEFT_ALIGNMENT);
            add(btTitle);
            
            add(Box.createVerticalStrut(8));
            
            bottleneckArea = new JTextArea(6, 25);
            bottleneckArea.setEditable(false);
            bottleneckArea.setFont(new Font("Consolas", Font.PLAIN, 11));
            bottleneckArea.setBackground(new Color(255, 250, 240));
            JScrollPane scroll = new JScrollPane(bottleneckArea);
            scroll.setAlignmentX(LEFT_ALIGNMENT);
            add(scroll);
        }
        
        private JLabel createMetricLabel(String name, String value) {
            JLabel label = new JLabel(
                "<html><b>" + name + ":</b><br>" +
                "<span style='font-size:18px; color:#2E86AB'>" + value + "</span></html>"
            );
            label.setAlignmentX(LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            return label;
        }
        
        public void updateStats() {
            Map<String, Object> stats = controller.getStatistics();
            
            int total = totalVehiclesSpawned;
            int active = vehicles.size();
            double cong = (double) stats.get("averageCongestion");
            double acc = (double) stats.get("predictionAccuracy");
            long uptime = (System.currentTimeMillis() - startTime) / 1000;
            int throughput = (int) (total / Math.max(1, uptime / 60.0));
            
            updateMetric(0, String.valueOf(total), new Color(46, 134, 171));
            updateMetric(1, String.valueOf(active), new Color(6, 167, 125));
            updateMetric(2, String.format("%.1f%%", cong * 100), 
                        cong > 0.6 ? new Color(230, 57, 70) : new Color(6, 167, 125));
            updateMetric(3, throughput + "/min", new Color(46, 134, 171));
            updateMetric(4, String.format("%.1fs", 2.0 + cong * 4), new Color(120, 120, 180));
            updateMetric(5, String.format("%.1f%%", acc * 100), new Color(6, 167, 125));
            updateMetric(6, String.format("%02d:%02d", uptime / 60, uptime % 60), new Color(100, 100, 120));
            
            // Bottlenecks
            Map<String, String> bts = controller.getBottlenecks();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, String> e : bts.entrySet()) {
                if (count++ < 3) {
                    sb.append("⚠ ").append(e.getKey()).append("\n");
                    sb.append("  ").append(e.getValue()).append("\n\n");
                }
            }
            if (bts.isEmpty()) {
                sb.append("✓ All systems normal\n  Traffic flowing smoothly");
            }
            bottleneckArea.setText(sb.toString());
        }
        
        private void updateMetric(int idx, String value, Color color) {
            String name = metricLabels[idx].getText().split(":")[0].replace("<html><b>", "");
            metricLabels[idx].setText(
                "<html><b>" + name + ":</b><br>" +
                "<span style='font-size:18px; color:rgb(" + color.getRed() + "," +
                color.getGreen() + "," + color.getBlue() + ")'>" + value + "</span></html>"
            );
        }
    }
    
    /**
     * Optimized charts panel with smooth animation
     */
    private class OptimizedChartsPanel extends JPanel {
        private LinkedList<Double> congestionData = new LinkedList<>();
        private LinkedList<Integer> vehicleData = new LinkedList<>();
        private static final int MAX_POINTS = 120; // 2 minutes at 1 sample/sec
        
        public OptimizedChartsPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(1000, 200));
        }
        
        public void updateChart() {
            Map<String, Object> stats = controller.getStatistics();
            double cong = (double) stats.get("averageCongestion");
            
            congestionData.add(cong);
            vehicleData.add(totalVehiclesSpawned);
            
            while (congestionData.size() > MAX_POINTS) {
                congestionData.removeFirst();
                vehicleData.removeFirst();
            }
            
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (congestionData.isEmpty()) return;
            
            int w = getWidth() - 80;
            int h = getHeight() - 60;
            int x0 = 50;
            int y0 = 30;
            
            // Axes
            g2d.setColor(new Color(180, 180, 190));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x0, y0 + h, x0 + w, y0 + h);
            g2d.drawLine(x0, y0, x0, y0 + h);
            
            // Grid
            g2d.setColor(new Color(230, 230, 235));
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i <= 4; i++) {
                int y = y0 + (h * i / 4);
                g2d.drawLine(x0, y, x0 + w, y);
            }
            
            // Congestion line
            if (congestionData.size() > 1) {
                g2d.setColor(new Color(231, 76, 60));
                g2d.setStroke(new BasicStroke(3));
                
                Path2D path = new Path2D.Double();
                boolean first = true;
                
                for (int i = 0; i < congestionData.size(); i++) {
                    int px = x0 + (i * w / MAX_POINTS);
                    int py = y0 + h - (int) (congestionData.get(i) * h);
                    
                    if (first) {
                        path.moveTo(px, py);
                        first = false;
                    } else {
                        path.lineTo(px, py);
                    }
                }
                
                g2d.draw(path);
            }
            
            // Labels
            g2d.setColor(new Color(60, 60, 80));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.drawString("Congestion Level", x0 + w - 130, y0 - 8);
            g2d.drawString("Time →", x0 + w - 50, y0 + h + 25);
            
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2d.drawString("100%", x0 - 35, y0 + 5);
            g2d.drawString("50%", x0 - 30, y0 + h/2 + 5);
            g2d.drawString("0%", x0 - 20, y0 + h + 5);
        }
    }
    
    /**
     * Optimized vehicle class
     */
    private class Vehicle {
        double x, y, vx, vy, angle;
        Color color;
        boolean active = true;
        int lifetime = 0;
        
        public Vehicle() {
            int gx = random.nextInt(GRID_SIZE);
            int gy = random.nextInt(GRID_SIZE);
            x = gx * CELL_SIZE + CELL_SIZE / 2;
            y = gy * CELL_SIZE + CELL_SIZE / 2;
            
            int dir = random.nextInt(4);
            switch (dir) {
                case 0: vx = 1.5; vy = 0; angle = 0; break;
                case 1: vx = -1.5; vy = 0; angle = Math.PI; break;
                case 2: vx = 0; vy = 1.5; angle = Math.PI/2; break;
                case 3: vx = 0; vy = -1.5; angle = -Math.PI/2; break;
            }
            
            Color[] colors = {
                new Color(231, 76, 60), new Color(52, 152, 219),
                new Color(46, 204, 113), new Color(241, 196, 15),
                new Color(155, 89, 182), new Color(230, 126, 34)
            };
            color = colors[random.nextInt(colors.length)];
        }
        
        public void update() {
            x += vx;
            y += vy;
            lifetime++;
            
            if (x < -20 || x > GRID_SIZE * CELL_SIZE + 20 ||
                y < -20 || y > GRID_SIZE * CELL_SIZE + 20 ||
                lifetime > 600) {
                active = false;
            }
        }
    }
    
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}
