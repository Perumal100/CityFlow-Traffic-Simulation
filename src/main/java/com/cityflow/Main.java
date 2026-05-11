package com.cityflow;

import com.cityflow.controller.CentralController;
import com.cityflow.database.DatabaseManager;
import com.cityflow.gui.SimulationGUI;
import com.cityflow.model.Intersection;
import com.cityflow.model.Vehicle;
import com.cityflow.network.SimulationServer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main entry point for CityFlow traffic simulation.
 * Initializes and coordinates all system components.
 */
public class Main {
    private static final int GRID_SIZE = 10;
    private static final int SERVER_PORT = 8080;
    private static final int VEHICLE_SPAWN_INTERVAL_MS = 2000; // 2 seconds
    
    private static List<Intersection> intersections;
    private static CentralController controller;
    private static SimulationServer server;
    private static ExecutorService intersectionExecutor;
    private static ExecutorService vehicleExecutor;
    private static ScheduledExecutorService vehicleSpawner;
    private static JFrame gui;
    private static DatabaseManager dbManager;
    private static volatile boolean running = true;
    
    public static void main(String[] args) {
        System.out.println("=== CityFlow Traffic Simulation ===");
        System.out.println("Initializing simulation...");
        
        // Initialize database
        initializeDatabase();
        
        // Create intersections
        intersections = createIntersections();
        
        // Start intersection threads
        startIntersections();
        
        // Create and start central controller
        controller = new CentralController(intersections);
        new Thread(controller, "CentralController").start();
        
        // Create and start network server
        server = new SimulationServer(SERVER_PORT, intersections, controller);
        new Thread(server, "SimulationServer").start();
        
        // Start vehicle spawning
        startVehicleSpawning();
        
        // Create and display optimized professional GUI
        SwingUtilities.invokeLater(() -> {
            gui = new com.cityflow.gui.OptimizedProfessionalGUI(intersections, controller);
            gui.setVisible(true);
        });
        
        // Add shutdown hook
        addShutdownHook();
        
        System.out.println("Simulation started successfully!");
        System.out.println("- Grid size: " + GRID_SIZE + "x" + GRID_SIZE);
        System.out.println("- Total intersections: " + intersections.size());
        System.out.println("- Server port: " + SERVER_PORT);
        System.out.println("- Connect clients with: java -cp bin com.cityflow.network.Client localhost " + SERVER_PORT);
    }
    
    /**
     * Initializes database connection
     */
    private static void initializeDatabase() {
        // Database temporarily disabled - will be initialized after first run
        // dbManager = DatabaseManager.getInstance();
        System.out.println("Database initialization skipped (will add after testing)");
    }
    
    /**
     * Creates the 10x10 grid of intersections
     */
    private static List<Intersection> createIntersections() {
        List<Intersection> list = new ArrayList<>();
        
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Intersection intersection = new Intersection(x, y);
                list.add(intersection);
            }
        }
        
        System.out.println("Created " + list.size() + " intersections");
        return list;
    }
    
    /**
     * Starts all intersection threads
     */
    private static void startIntersections() {
        intersectionExecutor = Executors.newFixedThreadPool(intersections.size());
        
        for (Intersection intersection : intersections) {
            intersectionExecutor.execute(intersection);
        }
        
        System.out.println("All intersection threads started");
    }
    
    /**
     * Starts periodic vehicle spawning
     */
    private static void startVehicleSpawning() {
        vehicleExecutor = Executors.newCachedThreadPool();
        vehicleSpawner = Executors.newScheduledThreadPool(1);
        
        vehicleSpawner.scheduleAtFixedRate(() -> {
            if (running) {
                spawnVehicle();
            }
        }, 0, VEHICLE_SPAWN_INTERVAL_MS, TimeUnit.MILLISECONDS);
        
        System.out.println("Vehicle spawning started");
    }
    
    /**
     * Spawns a new vehicle and adds it to a random intersection
     */
    private static void spawnVehicle() {
        String vehicleId = "V-" + System.currentTimeMillis();
        Vehicle vehicle = new Vehicle(vehicleId, GRID_SIZE);
        
        // Get starting intersection
        String startIntersection = vehicle.getPath().get(0);
        
        // Find intersection and add vehicle
        for (Intersection intersection : intersections) {
            if (intersection.getIntersectionId().equals(startIntersection)) {
                intersection.addVehicle(vehicle);
                vehicleExecutor.execute(vehicle);
                break;
            }
        }
    }
    
    /**
     * Adds shutdown hook for clean termination
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down CityFlow simulation...");
            shutdown();
        }));
    }
    
    /**
     * Gracefully shuts down all components
     */
    private static void shutdown() {
        running = false;
        
        // Stop vehicle spawning
        if (vehicleSpawner != null) {
            vehicleSpawner.shutdown();
            try {
                vehicleSpawner.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                vehicleSpawner.shutdownNow();
            }
        }
        
        // Stop vehicle threads
        if (vehicleExecutor != null) {
            vehicleExecutor.shutdown();
            try {
                vehicleExecutor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                vehicleExecutor.shutdownNow();
            }
        }
        
        // Stop intersections
        for (Intersection intersection : intersections) {
            intersection.stop();
        }
        
        if (intersectionExecutor != null) {
            intersectionExecutor.shutdown();
            try {
                intersectionExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                intersectionExecutor.shutdownNow();
            }
        }
        
        // Stop controller
        if (controller != null) {
            controller.stop();
        }
        
        // Stop server
        if (server != null) {
            server.shutdown();
        }
        
        // Stop GUI
        if (gui != null) {
            if (gui instanceof com.cityflow.gui.OptimizedProfessionalGUI) {
                ((com.cityflow.gui.OptimizedProfessionalGUI) gui).stopAnimation();
            } else if (gui instanceof com.cityflow.gui.ProfessionalGUI) {
                ((com.cityflow.gui.ProfessionalGUI) gui).stopAnimation();
            } else if (gui instanceof com.cityflow.gui.EnhancedSimulationGUI) {
                ((com.cityflow.gui.EnhancedSimulationGUI) gui).stopAnimation();
            }
            gui.dispose();
        }
        
        // Close database
        // if (dbManager != null) {
        //     dbManager.close();
        // }
        
        System.out.println("Shutdown complete");
    }
}
