package com.cityflow.network;

import java.io.*;
import java.net.Socket;

/**
 * Handles communication with a single connected client.
 * Runs on its own thread to process client commands.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final SimulationServer server;
    private BufferedReader input;
    private PrintWriter output;
    private volatile boolean running;
    
    public ClientHandler(Socket socket, SimulationServer server) {
        this.socket = socket;
        this.server = server;
        this.running = true;
        
        try {
            this.input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);
            
            // Send welcome message
            sendMessage("{\"type\":\"welcome\",\"message\":\"Connected to CityFlow simulation\"}");
            
        } catch (IOException e) {
            System.err.println("Error initializing client handler: " + e.getMessage());
            running = false;
        }
    }
    
    @Override
    public void run() {
        try {
            String command;
            while (running && (command = input.readLine()) != null) {
                handleCommand(command.trim());
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Client connection error: " + e.getMessage());
            }
        } finally {
            close();
        }
    }
    
    /**
     * Processes commands received from the client
     */
    private void handleCommand(String command) {
        if (command.isEmpty()) {
            return;
        }
        
        System.out.println("Received command: " + command);
        server.handleCommand(command, this);
    }
    
    /**
     * Sends a message to the client
     * @return true if successful, false if connection is broken
     */
    public boolean sendMessage(String message) {
        if (output == null || socket.isClosed()) {
            return false;
        }
        
        try {
            output.println(message);
            return !output.checkError();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Closes the client connection
     */
    public void close() {
        running = false;
        
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
        
        server.removeClient(this);
    }
}
