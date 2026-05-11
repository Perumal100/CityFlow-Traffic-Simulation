# CityFlow: Real-Time Traffic Simulation

A multithreaded Java application that simulates urban traffic flow with predictive analytics, adaptive signal control, and real-time visualization.

## 🎯 Project Overview

CityFlow models a 10×10 grid of intersections where each intersection operates independently on its own thread. A central controller monitors traffic density, predicts congestion using weighted moving averages, and adaptively adjusts signal timing to optimize traffic flow. The system supports multiple network clients and provides live visualization with animated traffic signals and congestion heatmaps.

## ✨ Key Features

### Core Functionality
- **100 Concurrent Intersection Threads** - Each intersection runs independently with its own signal cycle and vehicle queue
- **Predictive Traffic Analytics** - Machine learning-ready data collection with congestion prediction algorithms
- **Adaptive Signal Control** - Dynamic signal timing based on real-time and predicted congestion levels
- **Green Wave Coordination** - Synchronized signals along corridors for smoother traffic flow
- **Multi-threaded Vehicle Simulation** - Vehicles navigate the grid as independent threads

### Advanced Analytics
- **Weighted Moving Average Predictions** - Forecasts congestion 5 minutes ahead
- **Bottleneck Detection** - Identifies problematic intersections and spillover risks
- **Real-time Metrics** - Tracks throughput, wait times, and optimization effectiveness
- **Historical Data Analysis** - Comprehensive SQLite database for post-simulation review

### Networking & Visualization
- **Multi-Client Socket Server** - Connect multiple monitoring clients simultaneously
- **Live State Broadcasting** - 1-second updates to all connected clients
- **Animated GUI** - Real-time traffic grid with color-coded signals and congestion heatmap
- **Interactive Dashboard** - Live statistics and bottleneck analysis

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Main Application                      │
├─────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Intersection │  │ Intersection │  │ Intersection │  │
│  │   Thread 1   │  │   Thread 2   │  │  Thread 100  │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                  │                  │          │
│         └──────────────────┴──────────────────┘          │
│                            │                             │
│                   ┌────────▼────────┐                    │
│                   │ Central         │                    │
│                   │ Controller      │◄─┐                 │
│                   │ + Predictive    │  │                 │
│                   │   Analyzer      │  │                 │
│                   └────────┬────────┘  │                 │
│                            │            │                 │
│         ┌──────────────────┼────────────┘                │
│         │                  │                             │
│  ┌──────▼──────┐    ┌─────▼──────┐    ┌──────────────┐ │
│  │  Database   │    │ Network    │    │     GUI      │ │
│  │   Manager   │    │  Server    │    │  Animation   │ │
│  └─────────────┘    └────────────┘    └──────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## 🛠️ Technologies

- **Java 17+** - Core language
- **Swing** - GUI framework
- **SQLite** - Database (via JDBC)
- **JUnit 5** - Testing framework
- **Java Concurrency API** - ExecutorService, BlockingQueue, AtomicInteger

## 📋 Prerequisites

Before you begin, ensure you have:

- **Java JDK 17 or higher** installed
  - Download: [https://adoptium.net/](https://adoptium.net/)
  - Verify: `java -version`
- **Git** installed
  - Download: [https://git-scm.com/downloads](https://git-scm.com/downloads)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation
```

### 2. Download Dependencies

Download the SQLite JDBC driver and place it in the `lib/` folder:

```bash
# Create lib directory
mkdir -p lib

# Download SQLite JDBC driver
curl -L -o lib/sqlite-jdbc-3.45.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
```

Or download manually from: [https://github.com/xerial/sqlite-jdbc/releases](https://github.com/xerial/sqlite-jdbc/releases)

### 3. Compile the Project

```bash
# On Linux/Mac:
javac -d bin -cp "lib/*" $(find src/main/java -name "*.java")

# On Windows:
javac -d bin -cp "lib/*" src/main/java/com/cityflow/**/*.java
```

### 4. Run the Simulation

```bash
# On Linux/Mac:
java -cp "bin:lib/*" com.cityflow.Main

# On Windows:
java -cp "bin;lib/*" com.cityflow.Main
```

## 📊 Using the Application

### Main GUI Window

When you run the application, a window will open showing:

1. **Traffic Grid** - 10×10 grid with color-coded intersections
   - **Green background** - Low congestion
   - **Yellow background** - Moderate congestion  
   - **Red background** - High congestion
   - **Colored circles** - Traffic signal states (Red/Yellow/Green)
   - **Numbers** - Vehicle queue length at each intersection

2. **Statistics Panel** - Live metrics
   - Total vehicles processed
   - Average congestion level
   - Total queue length
   - Prediction accuracy

### Connecting Network Clients

The simulation automatically starts a server on port 8080. To connect a monitoring client:

```bash
telnet localhost 8080
```

You'll receive JSON updates every second with the current simulation state.

#### Available Commands

Send these commands to control the simulation:

- `STATS` - Get detailed statistics
- `PAUSE` - Pause the simulation
- `RESUME` - Resume the simulation
- `SPEED:2` - Adjust simulation speed (multiplier)

## 📁 Project Structure

```
CityFlow/
├── src/
│   ├── main/
│   │   └── java/com/cityflow/
│   │       ├── model/
│   │       │   ├── Intersection.java       # Intersection thread logic
│   │       │   ├── Vehicle.java            # Vehicle movement
│   │       │   └── TrafficSignal.java      # Signal states
│   │       ├── controller/
│   │       │   ├── CentralController.java  # Traffic coordination
│   │       │   └── PredictiveAnalyzer.java # Congestion prediction
│   │       ├── database/
│   │       │   └── DatabaseManager.java    # SQLite operations
│   │       ├── network/
│   │       │   ├── SimulationServer.java   # Socket server
│   │       │   └── ClientHandler.java      # Client connections
│   │       ├── gui/
│   │       │   └── SimulationGUI.java      # Main window
│   │       └── Main.java                   # Entry point
│   └── test/
│       └── java/com/cityflow/
│           └── IntersectionTest.java       # Unit tests
├── lib/
│   └── sqlite-jdbc-3.45.0.0.jar           # SQLite driver
├── docs/
│   └── SETUP.md                            # Setup guide
├── .gitignore
└── README.md
```

## 🧪 Running Tests

```bash
# Compile tests
javac -d bin -cp "bin:lib/*:lib/junit-platform-console-standalone-1.9.3.jar" \
  src/test/java/com/cityflow/*.java

# Run tests
java -jar lib/junit-platform-console-standalone-1.9.3.jar \
  --class-path bin --scan-class-path
```

## 📈 Database Schema

The simulation logs data to `cityflow.db` (created automatically):

### Tables

1. **intersection_metrics** - Time-series data for each intersection
2. **vehicle_journeys** - Complete vehicle trip records
3. **congestion_predictions** - Predicted vs actual congestion
4. **signal_optimizations** - Signal timing adjustments
5. **hourly_statistics** - Aggregated metrics

### Example Queries

```sql
-- Top 5 most congested intersections
SELECT intersection_id, AVG(congestion_level) as avg_congestion
FROM intersection_metrics
WHERE timestamp >= datetime('now', '-1 hour')
GROUP BY intersection_id
ORDER BY avg_congestion DESC
LIMIT 5;

-- Prediction accuracy over time
SELECT AVG(prediction_accuracy) as accuracy
FROM congestion_predictions
WHERE prediction_timestamp >= datetime('now', '-1 hour');
```

## 🔧 Configuration

Edit these constants in `Main.java` to customize:

```java
private static final int GRID_SIZE = 10;                    // Grid dimensions
private static final int SERVER_PORT = 8080;                // Network port
private static final int VEHICLE_SPAWN_INTERVAL_MS = 2000;  // Vehicle frequency
```

Edit signal timing in `TrafficSignal.java`:

```java
RED(5000),      // Red duration
YELLOW(2000),   // Yellow duration
GREEN(8000);    // Default green duration
```

## 🎓 Implementation Highlights

### Thread Safety
- `BlockingQueue` for vehicle queues (thread-safe)
- `synchronized` methods for signal state changes
- `AtomicInteger` for vehicle counters
- `ConcurrentHashMap` for density reports

### Concurrency Control
- Consistent lock ordering prevents deadlock
- ExecutorService manages thread pools efficiently
- Graceful shutdown with proper thread cleanup

### Predictive Algorithm
```
Weighted Moving Average:
prediction = Σ(weight[i] × history[i])
where weights prioritize recent data
```

## 👥 Team

- **Perumal Marimuthu** (pm4069)
- **Ashik John** (am15464)
- **Achyuthan Sivasankar** (as21154)

## 📝 License

This project is licensed under the MIT License - see below:

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.

Copyright (c) 2026 CityFlow Team
```

## 🐛 Troubleshooting

### "Class not found" error
- Ensure `lib/sqlite-jdbc-3.45.0.0.jar` is in the lib folder
- Check classpath includes both `bin` and `lib/*`

### Port 8080 already in use
- Change `SERVER_PORT` in Main.java
- Or stop other applications using port 8080

### Database locked error
- Close other instances of the simulation
- Delete `cityflow.db` and restart

### GUI not showing
- Ensure you're running Java 17+ with GUI support
- Try running with: `java -Djava.awt.headless=false ...`

## 🚀 Future Enhancements

- [ ] Support for different vehicle types (cars, buses, emergency)
- [ ] Pedestrian crossing simulation
- [ ] Weather conditions affecting traffic
- [ ] Machine learning model for long-term predictions
- [ ] Web-based dashboard using JavaFX WebView
- [ ] Export simulation replay videos

## 📞 Support

For questions or issues:
1. Check the troubleshooting section
2. Review existing GitHub issues
3. Create a new issue with detailed description

---

**Note**: This is an educational project for CS6103 - Introduction to Java
