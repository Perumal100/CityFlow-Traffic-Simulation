package com.cityflow.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a traffic intersection that runs on its own thread.
 * Manages traffic signals, vehicle queues, and reports metrics.
 */
public class Intersection implements Runnable {
    private final String intersectionId;
    private final int x;
    private final int y;
    private volatile TrafficSignal currentSignal;
    private volatile int greenDuration;
    private final BlockingQueue<Vehicle> vehicleQueue;
    private final AtomicInteger vehiclesProcessed;
    private volatile boolean running;
    private long lastSignalChange;
    private int queueLength;
    
    public Intersection(int x, int y) {
        this.intersectionId = x + "," + y;
        this.x = x;
        this.y = y;
        this.currentSignal = TrafficSignal.RED;
        this.greenDuration = TrafficSignal.GREEN.getDefaultDuration();
        this.vehicleQueue = new LinkedBlockingQueue<>();
        this.vehiclesProcessed = new AtomicInteger(0);
        this.running = true;
        this.lastSignalChange = System.currentTimeMillis();
        this.queueLength = 0;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastSignalChange;
                int signalDuration = getCurrentSignalDuration();
                
                // Check if it's time to change signal
                if (elapsedTime >= signalDuration) {
                    changeSignal();
                }
                
                // Process vehicles if signal is green
                if (currentSignal == TrafficSignal.GREEN) {
                    processVehicles();
                }
                
                // Update queue length
                queueLength = vehicleQueue.size();
                
                // Small sleep to prevent busy waiting
                Thread.sleep(100);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * Changes the traffic signal to the next state
     */
    private synchronized void changeSignal() {
        currentSignal = currentSignal.next();
        lastSignalChange = System.currentTimeMillis();
    }
    
    /**
     * Processes vehicles waiting at the intersection
     */
    private void processVehicles() {
        Vehicle vehicle = vehicleQueue.poll();
        if (vehicle != null) {
            vehiclesProcessed.incrementAndGet();
            // Vehicle continues on its journey (handled by vehicle thread)
        }
    }
    
    /**
     * Adds a vehicle to the intersection queue
     */
    public boolean addVehicle(Vehicle vehicle) {
        try {
            vehicleQueue.put(vehicle);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    /**
     * Adjusts green light duration based on traffic density
     */
    public synchronized void adjustGreenDuration(int newDuration) {
        this.greenDuration = Math.max(3000, Math.min(newDuration, 15000)); // 3-15 seconds
    }
    
    private int getCurrentSignalDuration() {
        if (currentSignal == TrafficSignal.GREEN) {
            return greenDuration;
        }
        return currentSignal.getDefaultDuration();
    }
    
    // Getters
    public String getIntersectionId() {
        return intersectionId;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public TrafficSignal getCurrentSignal() {
        return currentSignal;
    }
    
    public int getQueueLength() {
        return queueLength;
    }
    
    public int getVehiclesProcessed() {
        return vehiclesProcessed.get();
    }
    
    public int getGreenDuration() {
        return greenDuration;
    }
    
    public void stop() {
        this.running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Returns congestion level (0.0 to 1.0)
     */
    public double getCongestionLevel() {
        // Simple heuristic: queue length relative to capacity
        int capacity = 20; // arbitrary max queue before severe congestion
        return Math.min(1.0, (double) queueLength / capacity);
    }
}
