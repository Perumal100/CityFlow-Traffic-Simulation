package com.cityflow.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages database connections and schema initialization.
 * Uses SQLite for persistent storage of simulation metrics.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cityflow.db";
    private Connection connection;
    private static DatabaseManager instance;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initializes database connection and creates tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Database initialized successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    
    /**
     * Creates all necessary tables for the simulation
     */
    private void createTables() throws SQLException {
        String[] createTableQueries = {
            // Intersection metrics table
            """
            CREATE TABLE IF NOT EXISTS intersection_metrics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                intersection_id TEXT NOT NULL,
                timestamp DATETIME NOT NULL,
                queue_length INTEGER,
                vehicles_processed INTEGER,
                avg_wait_time_ms INTEGER,
                signal_state TEXT,
                controller_adjustment INTEGER,
                congestion_level REAL
            )
            """,
            
            // Vehicle journeys table
            """
            CREATE TABLE IF NOT EXISTS vehicle_journeys (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                vehicle_id TEXT NOT NULL,
                start_time DATETIME NOT NULL,
                end_time DATETIME,
                path TEXT,
                total_wait_time_ms INTEGER,
                distance_traveled INTEGER,
                trip_duration_ms INTEGER
            )
            """,
            
            // Congestion predictions table
            """
            CREATE TABLE IF NOT EXISTS congestion_predictions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                intersection_id TEXT NOT NULL,
                predicted_time DATETIME NOT NULL,
                predicted_congestion_level REAL,
                actual_congestion_level REAL,
                prediction_accuracy REAL,
                prediction_timestamp DATETIME NOT NULL
            )
            """,
            
            // Signal optimization logs
            """
            CREATE TABLE IF NOT EXISTS signal_optimizations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp DATETIME NOT NULL,
                intersection_id TEXT NOT NULL,
                old_timing INTEGER,
                new_timing INTEGER,
                reason TEXT,
                effectiveness_score REAL
            )
            """,
            
            // Aggregated statistics (for faster queries)
            """
            CREATE TABLE IF NOT EXISTS hourly_statistics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hour_start DATETIME NOT NULL,
                total_vehicles INTEGER,
                avg_wait_time_ms INTEGER,
                avg_trip_duration_ms INTEGER,
                peak_congestion_level REAL,
                throughput_per_minute REAL
            )
            """
        };
        
        for (String query : createTableQueries) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            }
        }
        
        // Create indices for faster queries
        createIndices();
    }
    
    /**
     * Creates database indices for performance optimization
     */
    private void createIndices() throws SQLException {
        String[] indexQueries = {
            "CREATE INDEX IF NOT EXISTS idx_intersection_time ON intersection_metrics(intersection_id, timestamp)",
            "CREATE INDEX IF NOT EXISTS idx_vehicle_time ON vehicle_journeys(start_time)",
            "CREATE INDEX IF NOT EXISTS idx_prediction_time ON congestion_predictions(predicted_time)",
            "CREATE INDEX IF NOT EXISTS idx_optimization_time ON signal_optimizations(timestamp)"
        };
        
        for (String query : indexQueries) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            }
        }
    }
    
    /**
     * Logs intersection metrics
     */
    public void logIntersectionMetrics(String intersectionId, int queueLength, 
                                       int vehiclesProcessed, int avgWaitTime, 
                                       String signalState, int adjustment, 
                                       double congestionLevel) {
        String sql = """
            INSERT INTO intersection_metrics 
            (intersection_id, timestamp, queue_length, vehicles_processed, 
             avg_wait_time_ms, signal_state, controller_adjustment, congestion_level)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, intersectionId);
            pstmt.setString(2, getCurrentTimestamp());
            pstmt.setInt(3, queueLength);
            pstmt.setInt(4, vehiclesProcessed);
            pstmt.setInt(5, avgWaitTime);
            pstmt.setString(6, signalState);
            pstmt.setInt(7, adjustment);
            pstmt.setDouble(8, congestionLevel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging intersection metrics: " + e.getMessage());
        }
    }
    
    /**
     * Logs vehicle journey completion
     */
    public void logVehicleJourney(String vehicleId, String startTime, String path,
                                  long totalWaitTime, int distanceTraveled, long tripDuration) {
        String sql = """
            INSERT INTO vehicle_journeys 
            (vehicle_id, start_time, end_time, path, total_wait_time_ms, 
             distance_traveled, trip_duration_ms)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vehicleId);
            pstmt.setString(2, startTime);
            pstmt.setString(3, getCurrentTimestamp());
            pstmt.setString(4, path);
            pstmt.setLong(5, totalWaitTime);
            pstmt.setInt(6, distanceTraveled);
            pstmt.setLong(7, tripDuration);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging vehicle journey: " + e.getMessage());
        }
    }
    
    /**
     * Logs congestion prediction
     */
    public void logPrediction(String intersectionId, double predictedLevel, 
                             double actualLevel, double accuracy) {
        String sql = """
            INSERT INTO congestion_predictions 
            (intersection_id, predicted_time, predicted_congestion_level, 
             actual_congestion_level, prediction_accuracy, prediction_timestamp)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, intersectionId);
            pstmt.setString(2, getCurrentTimestamp());
            pstmt.setDouble(3, predictedLevel);
            pstmt.setDouble(4, actualLevel);
            pstmt.setDouble(5, accuracy);
            pstmt.setString(6, getCurrentTimestamp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging prediction: " + e.getMessage());
        }
    }
    
    /**
     * Logs signal optimization event
     */
    public void logSignalOptimization(String intersectionId, int oldTiming, 
                                      int newTiming, String reason, double score) {
        String sql = """
            INSERT INTO signal_optimizations 
            (timestamp, intersection_id, old_timing, new_timing, reason, effectiveness_score)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, getCurrentTimestamp());
            pstmt.setString(2, intersectionId);
            pstmt.setInt(3, oldTiming);
            pstmt.setInt(4, newTiming);
            pstmt.setString(5, reason);
            pstmt.setDouble(6, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging optimization: " + e.getMessage());
        }
    }
    
    /**
     * Queries recent congestion data for analysis
     */
    public ResultSet getRecentCongestionData(int minutesBack) {
        String sql = """
            SELECT intersection_id, AVG(congestion_level) as avg_congestion, 
                   MAX(congestion_level) as peak_congestion
            FROM intersection_metrics
            WHERE timestamp >= datetime('now', '-' || ? || ' minutes')
            GROUP BY intersection_id
            ORDER BY avg_congestion DESC
            """;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, minutesBack);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error querying congestion data: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Gets average wait time over time for charting
     */
    public ResultSet getWaitTimeTrend(int minutesBack) {
        String sql = """
            SELECT datetime(timestamp) as time, AVG(avg_wait_time_ms) as avg_wait
            FROM intersection_metrics
            WHERE timestamp >= datetime('now', '-' || ? || ' minutes')
            GROUP BY strftime('%Y-%m-%d %H:%M', timestamp)
            ORDER BY time
            """;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, minutesBack);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error querying wait time trend: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Gets current timestamp in database format
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Closes database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
