package com.cityflow;

/**
 * Mutable simulation controls shared between {@link Main} and GUI scenario pages.
 */
public final class SimulationRuntime {
    private static volatile int vehicleSpawnIntervalMs = 380;
    private static volatile boolean spawningPaused = false;
    private static volatile double congestionBias = 0.0;

    private SimulationRuntime() {
    }

    public static int getVehicleSpawnIntervalMs() {
        return vehicleSpawnIntervalMs;
    }

    public static void setVehicleSpawnIntervalMs(int ms) {
        vehicleSpawnIntervalMs = Math.max(200, Math.min(12_000, ms));
    }

    public static boolean isSpawningPaused() {
        return spawningPaused;
    }

    public static void setSpawningPaused(boolean paused) {
        spawningPaused = paused;
    }

    /**
     * Artificial congestion offset for scenario testing (added to reported averages in UI only).
     */
    public static double getCongestionBias() {
        return congestionBias;
    }

    public static void setCongestionBias(double bias) {
        congestionBias = Math.max(0, Math.min(0.35, bias));
    }
}
