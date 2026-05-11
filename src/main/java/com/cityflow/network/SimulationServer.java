package com.cityflow.network;

import com.cityflow.controller.CentralController;
import com.cityflow.model.Intersection;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Socket server that handles multiple client connections.
 * Broadcasts simulation state and receives control commands.
 */
public class SimulationServer implements Runnable {
    private final int port;
    private final List<Intersection> intersections;
    private final CentralController controller;
    private final Set<ClientHandler> clients;
    private ServerSocket serverSocket;
    private volatile boolean running;
    private final ExecutorService clientExecutor;
    private final ScheduledExecutorService broadcaster;
    
    public SimulationServer(int port, List<Intersection> intersections, 
                           CentralController controller) {
        this.port = port;
        this.intersections = intersections;
        this.controller = controller;
        this.clients = ConcurrentHashMap.newKeySet();
        this.running = true;
        this.clientExecutor = Executors.newCachedThreadPool();
        this.broadcaster = Executors.newScheduledThreadPool(1);
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Simulation server started on port " + port);
            
            // Start broadcasting simulation state every second
            startBroadcasting();
            
            // Accept client connections
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + 
                        clientSocket.getInetAddress().getHostAddress());
                    
                    ClientHandler handler = new ClientHandler(clientSocket, this);
                    clients.add(handler);
                    clientExecutor.execute(handler);
                    
                } catch (SocketException e) {
                    if (!running) {
                        break; // Server is shutting down
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }
    
    /**
     * Starts periodic broadcasting of simulation state
     */
    private void startBroadcasting() {
        broadcaster.scheduleAtFixedRate(() -> {
            String stateJson = buildStateJson();
            broadcast(stateJson);
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    /**
     * Builds JSON representation of current simulation state
     */
    private String buildStateJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"type\":\"state_update\",");
        json.append("\"timestamp\":").append(System.currentTimeMillis()).append(",");
        json.append("\"intersections\":[");
        
        for (int i = 0; i < intersections.size(); i++) {
            Intersection intersection = intersections.get(i);
            if (i > 0) json.append(",");
            
            json.append("{");
            json.append("\"id\":\"").append(intersection.getIntersectionId()).append("\",");
            json.append("\"x\":").append(intersection.getX()).append(",");
            json.append("\"y\":").append(intersection.getY()).append(",");
            json.append("\"signal\":\"").append(intersection.getCurrentSignal()).append("\",");
            json.append("\"queue\":").append(intersection.getQueueLength()).append(",");
            json.append("\"processed\":").append(intersection.getVehiclesProcessed()).append(",");
            json.append("\"congestion\":").append(String.format("%.2f", intersection.getCongestionLevel()));
            json.append("}");
        }
        
        json.append("],");
        
        // Add statistics
        Map<String, Object> stats = controller.getStatistics();
        json.append("\"statistics\":{");
        json.append("\"totalVehicles\":").append(stats.get("totalVehiclesProcessed")).append(",");
        json.append("\"avgCongestion\":").append(String.format("%.2f", stats.get("averageCongestion"))).append(",");
        json.append("\"queueLength\":").append(stats.get("totalQueueLength")).append(",");
        json.append("\"accuracy\":").append(String.format("%.2f", stats.get("predictionAccuracy")));
        json.append("}");
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Broadcasts message to all connected clients
     */
    public void broadcast(String message) {
        Iterator<ClientHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientHandler client = iterator.next();
            if (!client.sendMessage(message)) {
                iterator.remove(); // Remove disconnected clients
            }
        }
    }
    
    /**
     * Handles commands received from clients
     */
    public void handleCommand(String command, ClientHandler sender) {
        try {
            String[] parts = command.split(":");
            String cmd = parts[0].toUpperCase();
            
            switch (cmd) {
                case "PAUSE":
                    pauseSimulation();
                    sender.sendMessage("{\"type\":\"ack\",\"message\":\"Simulation paused\"}");
                    break;
                    
                case "RESUME":
                    resumeSimulation();
                    sender.sendMessage("{\"type\":\"ack\",\"message\":\"Simulation resumed\"}");
                    break;
                    
                case "SPEED":
                    if (parts.length > 1) {
                        adjustSpeed(Integer.parseInt(parts[1]));
                        sender.sendMessage("{\"type\":\"ack\",\"message\":\"Speed adjusted\"}");
                    }
                    break;
                    
                case "STATS":
                    String statsJson = buildStatsJson();
                    sender.sendMessage(statsJson);
                    break;
                    
                default:
                    sender.sendMessage("{\"type\":\"error\",\"message\":\"Unknown command\"}");
            }
            
        } catch (Exception e) {
            sender.sendMessage("{\"type\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Builds detailed statistics JSON
     */
    private String buildStatsJson() {
        Map<String, Object> stats = controller.getStatistics();
        Map<String, String> bottlenecks = controller.getBottlenecks();
        
        StringBuilder json = new StringBuilder();
        json.append("{\"type\":\"statistics\",");
        json.append("\"data\":{");
        json.append("\"totalVehicles\":").append(stats.get("totalVehiclesProcessed")).append(",");
        json.append("\"avgCongestion\":").append(String.format("%.2f", stats.get("averageCongestion"))).append(",");
        json.append("\"predictionAccuracy\":").append(String.format("%.2f", stats.get("predictionAccuracy"))).append(",");
        json.append("\"bottlenecks\":[");
        
        int count = 0;
        for (Map.Entry<String, String> entry : bottlenecks.entrySet()) {
            if (count > 0) json.append(",");
            json.append("{\"intersection\":\"").append(entry.getKey()).append("\",");
            json.append("\"reason\":\"").append(entry.getValue()).append("\"}");
            count++;
        }
        
        json.append("]}}");
        return json.toString();
    }
    
    private void pauseSimulation() {
        // Implementation depends on your simulation control
        System.out.println("Pause command received");
    }
    
    private void resumeSimulation() {
        // Implementation depends on your simulation control
        System.out.println("Resume command received");
    }
    
    private void adjustSpeed(int speedFactor) {
        // Implementation depends on your simulation control
        System.out.println("Speed adjustment: " + speedFactor);
    }
    
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected. Active clients: " + clients.size());
    }
    
    public void shutdown() {
        running = false;
        
        // Close all client connections
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();
        
        // Shutdown executors
        broadcaster.shutdown();
        clientExecutor.shutdown();
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        
        System.out.println("Simulation server stopped");
    }
    
    public int getActiveClientCount() {
        return clients.size();
    }
}
