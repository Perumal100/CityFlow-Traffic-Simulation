package com.cityflow.controller;

import com.cityflow.SimulationRuntime;
import com.cityflow.database.DatabaseManager;
import com.cityflow.model.Intersection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Central controller that coordinates traffic signals across all intersections.
 * Implements adaptive signal timing and green wave coordination.
 */
public class CentralController implements Runnable {
    private final List<Intersection> intersections;
    private final Map<String, Integer> densityReports;
    private final DatabaseManager dbManager;
    private final PredictiveAnalyzer analyzer;
    private volatile boolean running;
    private final int updateIntervalMs = 1000; // 1 second

    /** Live dashboard: vehicles successfully queued for simulation (same instance as GUI). */
    private final AtomicLong simVehiclesSpawnedTotal = new AtomicLong();
    /** Vehicles currently executing {@link com.cityflow.model.Vehicle#run()}. */
    private final AtomicInteger simTripsInFlight = new AtomicInteger();

    public void recordSimVehicleSpawned() {
        simVehiclesSpawnedTotal.incrementAndGet();
    }

    public void beginSimTrip() {
        simTripsInFlight.incrementAndGet();
    }

    public void endSimTrip() {
        simTripsInFlight.updateAndGet(v -> Math.max(0, v - 1));
    }

    public CentralController(List<Intersection> intersections) {
        this.intersections = intersections;
        this.densityReports = new ConcurrentHashMap<>();
        this.dbManager = DatabaseManager.getInstance();
        this.analyzer = new PredictiveAnalyzer();
        this.running = true;
    }
    
    @Override
    public void run() {
        System.out.println("Central Controller started");
        
        while (running) {
            try {
                // Collect current state from all intersections
                collectDensityReports();
                
                // Update prediction model
                updatePredictions();
                
                // Identify congested intersections
                List<Intersection> congestedIntersections = identifyCongestion();
                
                // Apply adaptive timing adjustments
                applyAdaptiveTiming(congestedIntersections);
                
                // Coordinate green waves on main corridors
                coordinateGreenWaves();
                
                // Log controller activity
                logControllerActivity();
                
                Thread.sleep(updateIntervalMs);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Central Controller stopped");
    }
    
    /**
     * Collects queue length reports from all intersections
     */
    private void collectDensityReports() {
        for (Intersection intersection : intersections) {
            String id = intersection.getIntersectionId();
            int queueLength = intersection.getQueueLength();
            densityReports.put(id, queueLength);
            
            // Update prediction history
            double congestion = intersection.getCongestionLevel();
            analyzer.updateHistory(id, congestion);
        }
    }
    
    /**
     * Updates predictive models and logs predictions
     */
    private void updatePredictions() {
        for (Intersection intersection : intersections) {
            String id = intersection.getIntersectionId();
            double predicted = analyzer.predictCongestion(id);
            double actual = intersection.getCongestionLevel();
            double accuracy = analyzer.calculateAccuracy(predicted, actual);
            
            // Database logging disabled for now
            // dbManager.logPrediction(id, predicted, actual, accuracy);
        }
    }
    
    /**
     * Identifies intersections that need intervention
     */
    private List<Intersection> identifyCongestion() {
        List<Intersection> congested = new ArrayList<>();
        
        // Get top 10 predicted congested intersections
        List<PredictiveAnalyzer.CongestedIntersection> predictions = 
            analyzer.getTopCongestedIntersections(10);
        
        Set<String> congestedIds = new HashSet<>();
        for (PredictiveAnalyzer.CongestedIntersection ci : predictions) {
            congestedIds.add(ci.intersectionId);
        }
        
        // Find corresponding intersection objects
        for (Intersection intersection : intersections) {
            if (congestedIds.contains(intersection.getIntersectionId()) ||
                intersection.getCongestionLevel() > 0.5) {
                congested.add(intersection);
            }
        }
        
        return congested;
    }
    
    /**
     * Applies adaptive signal timing based on congestion levels
     */
    private void applyAdaptiveTiming(List<Intersection> congestedIntersections) {
        for (Intersection intersection : congestedIntersections) {
            String id = intersection.getIntersectionId();
            int currentTiming = intersection.getGreenDuration();
            
            // Get recommended timing from analyzer
            int recommendedTiming = analyzer.recommendSignalTiming(id, currentTiming);
            
            if (recommendedTiming != currentTiming) {
                intersection.adjustGreenDuration(recommendedTiming);
                
                // Database logging disabled for now
                /*
                String reason = "Congestion level: " + 
                    String.format("%.2f", intersection.getCongestionLevel());
                dbManager.logSignalOptimization(id, currentTiming, 
                    recommendedTiming, reason, 0.0);
                */
            }
        }
    }
    
    /**
     * Coordinates green waves along major corridors
     * Creates synchronized green lights on consecutive intersections
     */
    private void coordinateGreenWaves() {
        int gridSize = (int) Math.sqrt(intersections.size());
        
        // Coordinate horizontal corridors (rows)
        for (int row = 0; row < gridSize; row++) {
            coordinateRow(row, gridSize);
        }
        
        // Coordinate vertical corridors (columns)
        for (int col = 0; col < gridSize; col++) {
            coordinateColumn(col, gridSize);
        }
    }
    
    /**
     * Coordinates signals along a horizontal row
     */
    private void coordinateRow(int row, int gridSize) {
        // Find intersections in this row
        List<Intersection> rowIntersections = new ArrayList<>();
        
        for (Intersection intersection : intersections) {
            if (intersection.getY() == row) {
                rowIntersections.add(intersection);
            }
        }
        
        // Sort by x coordinate
        rowIntersections.sort(Comparator.comparingInt(Intersection::getX));
        
        // Calculate average congestion for this row
        double avgCongestion = rowIntersections.stream()
            .mapToDouble(Intersection::getCongestionLevel)
            .average()
            .orElse(0.0);
        
        // If row has moderate traffic, create green wave
        if (avgCongestion > 0.3 && avgCongestion < 0.7) {
            int baseTiming = 8000; // 8 seconds
            for (Intersection intersection : rowIntersections) {
                intersection.adjustGreenDuration(baseTiming);
            }
        }
    }
    
    /**
     * Coordinates signals along a vertical column
     */
    private void coordinateColumn(int col, int gridSize) {
        // Find intersections in this column
        List<Intersection> colIntersections = new ArrayList<>();
        
        for (Intersection intersection : intersections) {
            if (intersection.getX() == col) {
                colIntersections.add(intersection);
            }
        }
        
        // Sort by y coordinate
        colIntersections.sort(Comparator.comparingInt(Intersection::getY));
        
        // Calculate average congestion for this column
        double avgCongestion = colIntersections.stream()
            .mapToDouble(Intersection::getCongestionLevel)
            .average()
            .orElse(0.0);
        
        // If column has moderate traffic, create green wave
        if (avgCongestion > 0.3 && avgCongestion < 0.7) {
            int baseTiming = 8000; // 8 seconds
            for (Intersection intersection : colIntersections) {
                intersection.adjustGreenDuration(baseTiming);
            }
        }
    }
    
    /**
     * Logs controller activity and metrics
     */
    private void logControllerActivity() {
        // Database logging disabled for now
        /*
        for (Intersection intersection : intersections) {
            dbManager.logIntersectionMetrics(
                intersection.getIntersectionId(),
                intersection.getQueueLength(),
                intersection.getVehiclesProcessed(),
                0, // avg wait time (calculated separately)
                intersection.getCurrentSignal().toString(),
                0, // adjustment flag
                intersection.getCongestionLevel()
            );
        }
        */
    }
    
    /**
     * Gets statistics summary for display
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalVehicles = intersections.stream()
            .mapToInt(Intersection::getVehiclesProcessed)
            .sum();
        
        double avgCongestion = intersections.stream()
            .mapToDouble(Intersection::getCongestionLevel)
            .average()
            .orElse(0.0);
        avgCongestion = Math.min(1.0, avgCongestion + SimulationRuntime.getCongestionBias());

        int totalQueueLength = intersections.stream()
            .mapToInt(Intersection::getQueueLength)
            .sum();

        double maxCongestion = intersections.stream()
            .mapToDouble(Intersection::getCongestionLevel)
            .max()
            .orElse(0.0);

        // Fleet-wide queue pressure: vehicles are spawned randomly across 100 nodes, so per-cell
        // averages stay near zero even when the system is busy. This metric lifts the chart off 0%.
        final double queueScale = 10.0;
        double fleetQueuePressure = Math.min(1.0, totalQueueLength / queueScale);

        int tripsInFlight = simTripsInFlight.get();
        long simVehiclesSpawned = simVehiclesSpawnedTotal.get();

        // Tracks how "busy" the car population is: moving trips + physical queues (aligned with heavy spawn).
        double activityPulse = Math.min(1.0, (tripsInFlight * 2.2 + totalQueueLength) / 38.0);

        // Live chart blends geometry congestion with fleet backlog and live vehicle activity.
        double chartCongestion = Math.min(1.0,
            0.10 * avgCongestion + 0.24 * maxCongestion + 0.38 * fleetQueuePressure + 0.28 * activityPulse);

        // Dashboard congestion % stays tied to the same blend family.
        double congestionForKpi = Math.min(1.0,
            0.16 * avgCongestion + 0.52 * fleetQueuePressure + 0.32 * activityPulse);
        
        stats.put("totalVehiclesProcessed", totalVehicles);
        stats.put("averageCongestion", avgCongestion);
        stats.put("maxCongestion", maxCongestion);
        stats.put("fleetQueuePressure", fleetQueuePressure);
        stats.put("chartCongestion", chartCongestion);
        stats.put("chartActivityPulse", activityPulse);
        stats.put("congestionForKpi", congestionForKpi);
        stats.put("totalQueueLength", totalQueueLength);
        stats.put("simVehiclesSpawned", simVehiclesSpawned);
        stats.put("simTripsInFlight", tripsInFlight);
        stats.put("activeIntersections", intersections.size());
        stats.put("predictionAccuracy", analyzer.getOverallAccuracy());
        
        return stats;
    }
    
    /**
     * Gets bottleneck analysis
     */
    public Map<String, String> getBottlenecks() {
        return analyzer.analyzeBottlenecks(intersections);
    }

    /**
     * Row/column congestion snapshot for strategy and corridor views.
     */
    public List<String> getCorridorHealthLines() {
        int gridSize = (int) Math.sqrt(intersections.size());
        if (gridSize * gridSize != intersections.size()) {
            return List.of("Grid is not square; corridor summary unavailable.");
        }
        List<String> lines = new ArrayList<>();
        for (int row = 0; row < gridSize; row++) {
            final int r = row;
            double avg = intersections.stream()
                .filter(i -> i.getY() == r)
                .mapToDouble(Intersection::getCongestionLevel)
                .average()
                .orElse(0.0);
            String mode = (avg > 0.3 && avg < 0.7) ? "green-wave window" : "adaptive / local";
            lines.add(String.format("Row %2d  avg %3.0f%%  (%s)", row, avg * 100, mode));
        }
        for (int col = 0; col < gridSize; col++) {
            final int c = col;
            double avg = intersections.stream()
                .filter(i -> i.getX() == c)
                .mapToDouble(Intersection::getCongestionLevel)
                .average()
                .orElse(0.0);
            String mode = (avg > 0.3 && avg < 0.7) ? "green-wave window" : "adaptive / local";
            lines.add(String.format("Col %2d  avg %3.0f%%  (%s)", col, avg * 100, mode));
        }
        return lines;
    }

    public List<Intersection> getIntersectionsView() {
        return Collections.unmodifiableList(intersections);
    }
    
    public void stop() {
        this.running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
}
