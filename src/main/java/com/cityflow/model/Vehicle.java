package com.cityflow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a vehicle moving through the traffic grid.
 * Each vehicle runs as a separate thread and navigates between intersections.
 */
public class Vehicle implements Runnable {
    private final String vehicleId;
    private final List<String> path;
    private int currentIntersectionIndex;
    private long totalWaitTime;
    private final long startTime;
    private volatile boolean running;
    private final Random random;
    
    public Vehicle(String vehicleId, int gridSize) {
        this.vehicleId = vehicleId;
        this.path = generateRandomPath(gridSize);
        this.currentIntersectionIndex = 0;
        this.totalWaitTime = 0;
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.random = new Random();
    }
    
    /**
     * Generates a random path through the grid
     */
    private List<String> generateRandomPath(int gridSize) {
        List<String> route = new ArrayList<>();
        int startX = random.nextInt(gridSize);
        int startY = random.nextInt(gridSize);
        int endX = random.nextInt(gridSize);
        int endY = random.nextInt(gridSize);
        
        // Simple path: move horizontally then vertically
        int currentX = startX;
        int currentY = startY;
        
        route.add(currentX + "," + currentY);
        
        // Move horizontally
        while (currentX != endX) {
            currentX += (endX > currentX) ? 1 : -1;
            route.add(currentX + "," + currentY);
        }
        
        // Move vertically
        while (currentY != endY) {
            currentY += (endY > currentY) ? 1 : -1;
            route.add(currentX + "," + currentY);
        }
        
        return route;
    }
    
    @Override
    public void run() {
        while (running && currentIntersectionIndex < path.size()) {
            try {
                // Simulate travel time between intersections
                Thread.sleep(500 + random.nextInt(500));
                
                // Move to next intersection
                currentIntersectionIndex++;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public List<String> getPath() {
        return new ArrayList<>(path);
    }
    
    public String getCurrentIntersection() {
        if (currentIntersectionIndex < path.size()) {
            return path.get(currentIntersectionIndex);
        }
        return null;
    }
    
    public void addWaitTime(long waitMs) {
        this.totalWaitTime += waitMs;
    }
    
    public long getTotalWaitTime() {
        return totalWaitTime;
    }
    
    public long getTripDuration() {
        return System.currentTimeMillis() - startTime;
    }
    
    public int getDistanceTraveled() {
        return currentIntersectionIndex;
    }
    
    public void stop() {
        this.running = false;
    }
    
    public boolean isRunning() {
        return running && currentIntersectionIndex < path.size();
    }
}
