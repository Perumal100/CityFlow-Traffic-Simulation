package com.cityflow.controller;

import com.cityflow.database.DatabaseManager;
import com.cityflow.model.Intersection;

import java.util.*;

/**
 * Implements predictive analytics for traffic congestion.
 * Uses weighted moving average on historical data to predict future congestion.
 */
public class PredictiveAnalyzer {
    private final DatabaseManager dbManager;
    private final Map<String, List<Double>> congestionHistory;
    private final int historyWindowSize = 10;
    private final double[] weights = {0.05, 0.07, 0.08, 0.10, 0.12, 0.13, 0.15, 0.15, 0.10, 0.05}; // Weights for weighted moving average
    
    public PredictiveAnalyzer() {
        this.dbManager = DatabaseManager.getInstance();
        this.congestionHistory = new HashMap<>();
    }
    
    /**
     * Updates congestion history for an intersection
     */
    public void updateHistory(String intersectionId, double congestionLevel) {
        congestionHistory.putIfAbsent(intersectionId, new ArrayList<>());
        List<Double> history = congestionHistory.get(intersectionId);
        
        history.add(congestionLevel);
        
        // Keep only recent history
        if (history.size() > historyWindowSize) {
            history.remove(0);
        }
    }
    
    /**
     * Predicts congestion level for next time period using weighted moving average
     */
    public double predictCongestion(String intersectionId) {
        List<Double> history = congestionHistory.get(intersectionId);
        
        if (history == null || history.isEmpty()) {
            return 0.0;
        }
        
        // Remove any null values
        history.removeIf(val -> val == null);
        
        if (history.isEmpty()) {
            return 0.0;
        }
        
        // Use weighted moving average
        double prediction = 0.0;
        int historySize = history.size();
        
        for (int i = 0; i < historySize; i++) {
            Double value = history.get(i);
            if (value != null) {
                int weightIndex = Math.max(0, weights.length - historySize + i);
                prediction += value * weights[weightIndex];
            }
        }
        
        // Normalize if we don't have full history
        if (historySize < historyWindowSize) {
            double totalWeight = 0.0;
            for (int i = 0; i < historySize; i++) {
                int weightIndex = Math.max(0, weights.length - historySize + i);
                totalWeight += weights[weightIndex];
            }
            if (totalWeight > 0) {
                prediction /= totalWeight;
            }
        }
        
        return Math.min(1.0, Math.max(0.0, prediction));
    }
    
    /**
     * Calculates prediction accuracy by comparing predicted vs actual
     */
    public double calculateAccuracy(double predicted, double actual) {
        double error = Math.abs(predicted - actual);
        return Math.max(0.0, 1.0 - error);
    }
    
    /**
     * Identifies top N most congested intersections based on predictions
     */
    public List<CongestedIntersection> getTopCongestedIntersections(int topN) {
        List<CongestedIntersection> congested = new ArrayList<>();
        
        for (Map.Entry<String, List<Double>> entry : congestionHistory.entrySet()) {
            String id = entry.getKey();
            double prediction = predictCongestion(id);
            
            if (prediction > 0.3) { // Only consider significantly congested
                congested.add(new CongestedIntersection(id, prediction));
            }
        }
        
        // Sort by predicted congestion level (descending)
        congested.sort((a, b) -> Double.compare(b.congestionLevel, a.congestionLevel));
        
        return congested.subList(0, Math.min(topN, congested.size()));
    }
    
    /**
     * Analyzes congestion patterns to detect bottlenecks
     */
    public Map<String, String> analyzeBottlenecks(List<Intersection> intersections) {
        Map<String, String> bottlenecks = new HashMap<>();
        
        for (Intersection intersection : intersections) {
            String id = intersection.getIntersectionId();
            double currentCongestion = intersection.getCongestionLevel();
            double predictedCongestion = predictCongestion(id);
            
            // Identify persistent congestion
            if (currentCongestion > 0.6 && predictedCongestion > 0.6) {
                bottlenecks.put(id, "Persistent high congestion - needs intervention");
            }
            // Identify growing congestion
            else if (predictedCongestion > currentCongestion + 0.2) {
                bottlenecks.put(id, "Rapidly increasing congestion - preemptive action needed");
            }
            // Identify spillover risk
            else if (currentCongestion > 0.7) {
                bottlenecks.put(id, "Critical congestion - spillover risk to adjacent intersections");
            }
        }
        
        return bottlenecks;
    }
    
    /**
     * Recommends signal timing adjustment based on prediction
     */
    public int recommendSignalTiming(String intersectionId, int currentTiming) {
        double prediction = predictCongestion(id);
        
        if (prediction > 0.7) {
            // High congestion: increase green time by 50%
            return (int) (currentTiming * 1.5);
        } else if (prediction > 0.5) {
            // Moderate congestion: increase by 25%
            return (int) (currentTiming * 1.25);
        } else if (prediction < 0.2) {
            // Low congestion: decrease to minimum
            return (int) (currentTiming * 0.8);
        }
        
        return currentTiming; // No change needed
    }
    
    /**
     * Calculates average prediction accuracy across all intersections
     */
    public double getOverallAccuracy() {
        if (congestionHistory.isEmpty()) {
            return 0.0;
        }
        
        // Calculate accuracy from in-memory data instead of database
        double totalAccuracy = 0.0;
        int count = 0;
        
        for (Map.Entry<String, List<Double>> entry : congestionHistory.entrySet()) {
            if (entry.getValue().size() >= 2) {
                List<Double> history = entry.getValue();
                // Compare last prediction with current actual
                double predicted = predictCongestion(entry.getKey());
                double actual = history.get(history.size() - 1);
                totalAccuracy += calculateAccuracy(predicted, actual);
                count++;
            }
        }
        
        return count > 0 ? totalAccuracy / count : 0.0;
    }
    
    /**
     * Gets congestion trend (increasing, decreasing, stable)
     */
    public String getCongestionTrend(String intersectionId) {
        List<Double> history = congestionHistory.get(intersectionId);
        
        if (history == null || history.size() < 3) {
            return "INSUFFICIENT_DATA";
        }
        
        double recent = 0.0;
        double older = 0.0;
        int halfSize = history.size() / 2;
        
        // Compare recent half vs older half
        for (int i = 0; i < halfSize; i++) {
            older += history.get(i);
        }
        for (int i = halfSize; i < history.size(); i++) {
            recent += history.get(i);
        }
        
        recent /= (history.size() - halfSize);
        older /= halfSize;
        
        double diff = recent - older;
        
        if (diff > 0.1) {
            return "INCREASING";
        } else if (diff < -0.1) {
            return "DECREASING";
        } else {
            return "STABLE";
        }
    }
    
    /**
     * Inner class to represent congested intersection
     */
    public static class CongestedIntersection {
        public final String intersectionId;
        public final double congestionLevel;
        
        public CongestedIntersection(String intersectionId, double congestionLevel) {
            this.intersectionId = intersectionId;
            this.congestionLevel = congestionLevel;
        }
    }
}
