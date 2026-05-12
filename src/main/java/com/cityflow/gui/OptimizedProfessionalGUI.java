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
 * Ultra-optimized professional dashboard (map, KPIs, charts). Embedded in {@link CityFlowShellFrame} tabs.
 */
public class OptimizedProfessionalGUI extends JPanel {
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
        
        setLayout(new BorderLayout(0, 0));
        setOpaque(true);
        setBackground(UiTheme.BG_DEEP);
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UiTheme.BG_APP);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));
        
        // Create panels
        mapPanel = new OptimizedMapPanel();
        statsPanel = new OptimizedStatsPanel();
        chartsPanel = new OptimizedChartsPanel();
        
        // Map (center)
        JScrollPane mapScroll = new JScrollPane(mapPanel);
        mapScroll.setBorder(wrapTitled("Live city traffic map"));
        UiTheme.styleScroll(mapScroll);
        mainPanel.add(mapScroll, BorderLayout.CENTER);
        
        // Stats (right)
        JScrollPane statsScroll = new JScrollPane(statsPanel);
        statsScroll.setBorder(wrapTitled("Analytics dashboard"));
        statsScroll.setPreferredSize(new Dimension(360, 600));
        UiTheme.styleScroll(statsScroll);
        mainPanel.add(statsScroll, BorderLayout.EAST);
        
        // Charts (bottom)
        chartsPanel.setBorder(wrapTitled("Live congestion — sim cars, queues, grid"));
        chartsPanel.setPreferredSize(new Dimension(1000, 200));
        mainPanel.add(chartsPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Spawn initial vehicles
        spawnVehicles(85);
        
        // Start optimized animation
        startOptimizedAnimation();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x162032), w, h, new Color(0x0c1520));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
                g2.setColor(new Color(77, 171, 247, 60));
                g2.fillRect(0, h - 2, w, 2);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("🚦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        left.add(icon);
        JLabel title = new JLabel("Live operations center");
        title.setForeground(UiTheme.TEXT);
        title.setFont(UiTheme.fontUi(Font.BOLD, 18));
        left.add(title);
        JLabel sub = new JLabel("  ·  10×10 grid  ·  predictive signals");
        sub.setForeground(UiTheme.TEXT_MUTED);
        sub.setFont(UiTheme.fontUi(Font.PLAIN, 13));
        left.add(sub);
        header.add(left, BorderLayout.WEST);
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        right.setOpaque(false);
        JLabel live = new JLabel("● LIVE");
        live.setForeground(UiTheme.SUCCESS);
        live.setFont(UiTheme.fontUi(Font.BOLD, 13));
        right.add(live);
        JLabel ints = new JLabel("100 nodes");
        ints.setForeground(UiTheme.TEXT_MUTED);
        ints.setFont(UiTheme.fontUi(Font.PLAIN, 13));
        right.add(ints);
        header.add(right, BorderLayout.EAST);
        return header;
    }
    
    private javax.swing.border.Border wrapTitled(String title) {
        return BorderFactory.createCompoundBorder(
            UiTheme.titled(title),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
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
            
            // Spawn new vehicles occasionally (visual layer — denser for busier map)
            if (frameCount % 22 == 0 && vehicles.size() < 100) {
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
            
            if (frameCount % 6 == 0) {
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
            setBackground(UiTheme.BG_CARD_INSET);
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
            g2d.setColor(UiTheme.BG_CARD_INSET);
            g2d.fillRect(0, 0, width, height);
            
            // Roads
            g2d.setColor(new Color(0x334155));
            g2d.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            for (int i = 0; i <= GRID_SIZE; i++) {
                int y = offsetY + i * CELL_SIZE;
                g2d.drawLine(offsetX, y, offsetX + GRID_SIZE * CELL_SIZE, y);
                
                int x = offsetX + i * CELL_SIZE;
                g2d.drawLine(x, offsetY, x, offsetY + GRID_SIZE * CELL_SIZE);
            }
            
            // Lane marks
            g2d.setColor(new Color(250, 204, 21, 140));
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
            Color heat = cong < 0.3 ? new Color(34, 197, 94, 100) :
                        cong < 0.6 ? new Color(250, 204, 21, 110) :
                        new Color(248, 113, 113, 120);
            g2d.setColor(heat);
            g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            
            // Intersection platform
            g2d.setColor(new Color(0x475569));
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
        private final String[] metricRowNames = {
            "Sim vehicles spawned",
            "Trips in flight",
            "Congestion %",
            "Spawns / min",
            "Cars in queues",
            "Prediction accuracy",
            "Uptime"
        };
        
        public OptimizedStatsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(UiTheme.BG_CARD);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
            
            add(Box.createVerticalStrut(10));
            
            JLabel title = new JLabel("LIVE METRICS");
            title.setFont(UiTheme.fontUi(Font.BOLD, 14));
            title.setForeground(UiTheme.TEXT);
            title.setAlignmentX(LEFT_ALIGNMENT);
            add(title);
            
            add(Box.createVerticalStrut(15));
            
            for (int i = 0; i < metricRowNames.length; i++) {
                metricLabels[i] = createMetricLabel(metricRowNames[i], "0");
                add(metricLabels[i]);
                add(Box.createVerticalStrut(10));
            }
            
            add(Box.createVerticalStrut(15));
            
            JLabel btTitle = new JLabel("Alerts");
            btTitle.setFont(UiTheme.fontUi(Font.BOLD, 13));
            btTitle.setForeground(UiTheme.WARNING);
            btTitle.setAlignmentX(LEFT_ALIGNMENT);
            add(btTitle);
            
            add(Box.createVerticalStrut(8));
            
            bottleneckArea = new JTextArea(6, 25);
            bottleneckArea.setEditable(false);
            bottleneckArea.setFont(UiTheme.fontMono(Font.PLAIN, 11));
            bottleneckArea.setBackground(UiTheme.BG_CARD_INSET);
            bottleneckArea.setForeground(UiTheme.TEXT);
            bottleneckArea.setCaretColor(UiTheme.ACCENT);
            bottleneckArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            JScrollPane scroll = new JScrollPane(bottleneckArea);
            scroll.setAlignmentX(LEFT_ALIGNMENT);
            UiTheme.styleScroll(scroll);
            add(scroll);
        }
        
        private JLabel createMetricLabel(String name, String value) {
            JLabel label = new JLabel(
                "<html><b><span style=\"color:#94a3b8;\">" + name + "</span></b><br>"
                    + "<span style=\"font-size:20px;color:#7dd3fc;\">" + value + "</span></html>"
            );
            label.setAlignmentX(LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            return label;
        }
        
        public void updateStats() {
            Map<String, Object> stats = controller.getStatistics();
            
            long total = toLong(stats.get("simVehiclesSpawned"));
            int inflight = toInt(stats.get("simTripsInFlight"));
            int queued = toInt(stats.get("totalQueueLength"));
            double cong = toDouble(stats.get("congestionForKpi"));
            double acc = toDouble(stats.get("predictionAccuracy"));
            long uptime = (System.currentTimeMillis() - startTime) / 1000;
            int spawnsPerMin = (int) (total / Math.max(1, uptime / 60.0));
            
            updateMetric(0, String.valueOf(total), UiTheme.ACCENT);
            updateMetric(1, String.valueOf(inflight), UiTheme.SUCCESS);
            updateMetric(2, String.format("%.1f%%", cong * 100), 
                        cong > 0.6 ? UiTheme.DANGER : UiTheme.SUCCESS);
            updateMetric(3, spawnsPerMin + "/min", UiTheme.ACCENT_GLOW);
            updateMetric(4, String.valueOf(queued), queued > 45 ? UiTheme.DANGER : UiTheme.ACCENT_VIOLET);
            updateMetric(5, String.format("%.1f%%", acc * 100), UiTheme.SUCCESS);
            updateMetric(6, String.format("%02d:%02d", uptime / 60, uptime % 60), UiTheme.TEXT_MUTED);
            
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
            String name = metricRowNames[idx];
            metricLabels[idx].setText(
                "<html><b><span style=\"color:#94a3b8;\">" + name + "</span></b><br>"
                    + "<span style=\"font-size:20px;color:rgb(" + color.getRed() + ","
                    + color.getGreen() + "," + color.getBlue() + ");\">" + value + "</span></html>"
            );
        }

        private static long toLong(Object o) {
            return o instanceof Number ? ((Number) o).longValue() : 0L;
        }

        private static int toInt(Object o) {
            return o instanceof Number ? ((Number) o).intValue() : 0;
        }

        private static double toDouble(Object o) {
            return o instanceof Number ? ((Number) o).doubleValue() : 0.0;
        }
    }
    
    /**
     * Optimized charts panel with smooth animation
     */
        private class OptimizedChartsPanel extends JPanel {
        private final LinkedList<Double> chartSeries = new LinkedList<>();
        private final LinkedList<Double> avgSeries = new LinkedList<>();
        private final LinkedList<Double> activitySeries = new LinkedList<>();
        private static final int MAX_POINTS = 280;

        public OptimizedChartsPanel() {
            setBackground(UiTheme.BG_CARD);
            setPreferredSize(new Dimension(1000, 200));
        }

        public void updateChart() {
            Map<String, Object> stats = controller.getStatistics();
            Object chartObj = stats.get("chartCongestion");
            double chartVal = chartObj instanceof Double ? (Double) chartObj : (double) stats.get("averageCongestion");
            Object fleetObj = stats.get("fleetQueuePressure");
            double avgVal = fleetObj instanceof Double ? (Double) fleetObj : (double) stats.get("averageCongestion");
            Object pulseObj = stats.get("chartActivityPulse");
            double pulseVal = pulseObj instanceof Double ? (Double) pulseObj : 0.0;

            chartSeries.add(chartVal);
            avgSeries.add(avgVal);
            activitySeries.add(pulseVal);

            while (chartSeries.size() > MAX_POINTS) {
                chartSeries.removeFirst();
                avgSeries.removeFirst();
                activitySeries.removeFirst();
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (chartSeries.isEmpty()) {
                return;
            }

            int w = getWidth() - 80;
            int h = getHeight() - 60;
            int x0 = 50;
            int y0 = 30;

            // Axes
            g2d.setColor(UiTheme.BORDER_SOFT);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x0, y0 + h, x0 + w, y0 + h);
            g2d.drawLine(x0, y0, x0, y0 + h);

            // Grid
            g2d.setColor(new Color(0x1e293b));
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i <= 4; i++) {
                int y = y0 + (h * i / 4);
                g2d.drawLine(x0, y, x0 + w, y);
            }

            int n = chartSeries.size();
            int span = Math.max(1, n - 1);

            // Filled area under primary (peak-weighted) series
            if (n > 1) {
                Path2D fill = new Path2D.Double();
                fill.moveTo(x0, y0 + h);
                int lastPx = x0;
                for (int i = 0; i < n; i++) {
                    int px = x0 + (int) ((long) i * w / span);
                    int py = y0 + h - (int) (chartSeries.get(i) * h);
                    fill.lineTo(px, py);
                    lastPx = px;
                }
                fill.lineTo(lastPx, y0 + h);
                fill.lineTo(x0, y0 + h);
                fill.closePath();
                g2d.setPaint(new GradientPaint(x0, y0, new Color(77, 171, 247, 55), x0, y0 + h, new Color(77, 171, 247, 0)));
                g2d.fill(fill);
            }

            // Fleet queue pressure (dashed)
            if (avgSeries.size() > 1) {
                g2d.setColor(new Color(148, 163, 184, 160));
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6, 5}, 0));
                Path2D avgPath = new Path2D.Double();
                boolean first = true;
                for (int i = 0; i < avgSeries.size(); i++) {
                    int px = x0 + (int) ((long) i * w / span);
                    int py = y0 + h - (int) (avgSeries.get(i) * h);
                    if (first) {
                        avgPath.moveTo(px, py);
                        first = false;
                    } else {
                        avgPath.lineTo(px, py);
                    }
                }
                g2d.draw(avgPath);
            }

            // Sim activity: in-flight trips + queued cars (amber)
            if (activitySeries.size() > 1) {
                g2d.setColor(new Color(251, 191, 36, 200));
                g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D pulsePath = new Path2D.Double();
                boolean first = true;
                for (int i = 0; i < activitySeries.size(); i++) {
                    int px = x0 + (int) ((long) i * w / span);
                    int py = y0 + h - (int) (activitySeries.get(i) * h);
                    if (first) {
                        pulsePath.moveTo(px, py);
                        first = false;
                    } else {
                        pulsePath.lineTo(px, py);
                    }
                }
                g2d.draw(pulsePath);
            }

            // Primary: blended congestion including sim vehicle pressure
            if (chartSeries.size() > 1) {
                g2d.setColor(UiTheme.ACCENT);
                g2d.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D path = new Path2D.Double();
                boolean first = true;
                for (int i = 0; i < chartSeries.size(); i++) {
                    int px = x0 + (int) ((long) i * w / span);
                    int py = y0 + h - (int) (chartSeries.get(i) * h);
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
            g2d.setColor(UiTheme.TEXT_MUTED);
            g2d.setFont(UiTheme.fontUi(Font.BOLD, 11));
            g2d.drawString("Cyan = congestion blend (grid + sim cars)", x0, y0 - 18);
            g2d.drawString("Gray dashes = Σ queues   ·   Amber = in-flight + queued cars", x0, y0 - 4);
            g2d.setFont(UiTheme.fontUi(Font.BOLD, 11));
            g2d.drawString("Time →", x0 + w - 50, y0 + h + 25);

            g2d.setFont(UiTheme.fontUi(Font.PLAIN, 10));
            g2d.drawString("100%", x0 - 35, y0 + 5);
            g2d.drawString("50%", x0 - 30, y0 + h / 2 + 5);
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
