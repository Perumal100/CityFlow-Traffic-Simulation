package com.cityflow.model;

/**
 * Represents the state of a traffic signal at an intersection.
 * Includes timing information for each phase.
 */
public enum TrafficSignal {
    RED(5000),      // 5 seconds
    YELLOW(2000),   // 2 seconds
    GREEN(8000);    // 8 seconds (default, can be adjusted)
    
    private final int defaultDuration; // in milliseconds
    
    TrafficSignal(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
    
    public int getDefaultDuration() {
        return defaultDuration;
    }
    
    /**
     * Returns the next signal state in the cycle
     */
    public TrafficSignal next() {
        switch (this) {
            case RED:
                return GREEN;
            case GREEN:
                return YELLOW;
            case YELLOW:
                return RED;
            default:
                return RED;
        }
    }
}
