# 🚦 CityFlow Traffic Simulation System

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Active-success)
![Threads](https://img.shields.io/badge/Threads-100+-blue)

Real-time multi-threaded traffic simulation with predictive analytics, adaptive signal control, and professional GUI visualization.

---

## 👥 Project Team

This project was developed as part of **CS6103 - Introduction to Java**

**Team Members:**
- **Perumal Marimuthu** - [@Perumal100](https://github.com/Perumal100) - Lead Developer & System Architecture
- **Ashik John** - Database Design & Predictive Analytics
- **Achyuthan Sivasankar** - GUI Development & Testing

**Institution:** New York University Tandon School of Engineering  
**Semester:** Spring 2026  
**Course:** CS6103 - Introduction to Java

---

## 🎯 Project Overview

CityFlow is a sophisticated traffic management simulation that models a **10×10 city grid** with **100 intersections**, each operating as an independent thread. The system features:

- 🚗 **Real-time vehicle simulation** with 40-60 concurrent vehicles
- 🧠 **Predictive analytics** using weighted moving average algorithms
- 🎛️ **Adaptive signal control** that adjusts timing based on congestion
- 🌊 **Green wave coordination** for synchronized traffic flow
- 📊 **Professional GUI** with live statistics and performance charts
- 🌐 **Multi-client network server** for remote monitoring

---

## ✨ Key Features

### 🔄 Concurrency & Threading
- **100 independent intersection threads** - Each intersection manages its own signal cycle and vehicle queue
- **Thread-safe collections** - BlockingQueue, CopyOnWriteArrayList for concurrent access
- **Synchronized signal control** - Prevents race conditions and ensures data consistency
- **Graceful shutdown** - Proper thread cleanup and resource management

### 🧠 Intelligent Traffic Management
- **Predictive congestion forecasting** - Weighted moving average predicts traffic 5 minutes ahead
- **Adaptive signal timing** - Dynamically adjusts green light duration (6-12 seconds) based on queue length
- **Bottleneck detection** - Identifies problematic intersections before critical congestion
- **Green wave optimization** - Synchronizes signals along corridors for smooth flow

### 📊 Real-time Analytics
- **Live metrics tracking** - Vehicles processed, throughput, average congestion
- **Prediction accuracy monitoring** - Tracks effectiveness of forecasting algorithms
- **Performance visualization** - 2-minute rolling charts showing congestion trends
- **Bottleneck alerts** - Real-time warnings for problem intersections

### 🎨 Professional GUI
- **Smooth 60 FPS animation** - Optimized rendering with cached road layers
- **Realistic vehicle models** - 40+ cars with different colors and movement
- **Color-coded heatmap** - Green (low), Yellow (moderate), Red (high congestion)
- **Interactive traffic lights** - 3-bulb design with glow effects
- **Auto-maximized window** - Opens full-screen for best experience

---

## 🐳 Quick Start with Docker (Easiest Way!)

**Run the entire simulation with ONE command - no Java or Eclipse needed!**

### Prerequisites
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop)
- **Windows users also need:** [VcXsrv](https://sourceforge.net/projects/vcxsrv/) (X Server)

### Run in 3 Steps

```bash
# 1. Clone repository
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git

# 2. Navigate to folder
cd CityFlow-Traffic-Simulation

# 3. Run with Docker Compose
docker-compose up --build
```

**That's it!** The GUI window will appear and the simulation will start automatically.

### Windows Users: One Extra Step

Before running Docker, you need an X Server for GUI display:

1. Download and install [VcXsrv](https://sourceforge.net/projects/vcxsrv/)
2. Run **XLaunch** (from Start Menu)
3. Configuration wizard:
   - Select: **"Multiple windows"**
   - Display number: **0**
   - Click **Next**
   - Select: **"Start no client"**
   - Click **Next**
   - ✅ **Check "Disable access control"** (IMPORTANT!)
   - Click **Finish**
4. VcXsrv will run in the background (system tray icon)
5. Now run: `docker-compose up --build`

### Full Docker Documentation
See [DOCKER_SETUP.md](DOCKER_SETUP.md) for comprehensive instructions, troubleshooting, and advanced usage.

---

## 🖥️ Traditional Setup (Without Docker)

If you prefer to run without Docker, you'll need Java and an IDE.

### Prerequisites
- **Java 17 or higher** - [Download from Adoptium](https://adoptium.net/)
- **Eclipse IDE** (recommended) or any Java IDE
- **Git** - [Download here](https://git-scm.com/downloads)

### Installation Steps

#### 1. Clone the Repository
```bash
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation
```

#### 2. Import to Eclipse

1. Open Eclipse
2. **File** → **Import**
3. Select **General** → **Existing Projects into Workspace**
4. Click **Next**
5. **Browse** to the `CityFlow-Traffic-Simulation` folder
6. Ensure the project is checked
7. Click **Finish**

#### 3. Clean and Build

This is **CRITICAL** - don't skip this step!

1. **Project** → **Clean**
2. Select **CityFlow** project
3. Click **OK**
4. Wait for "Building workspace" to complete (bottom-right of Eclipse)

#### 4. Run the Application

1. In **Package Explorer**, expand:
   ```
   CityFlow → src/main/java → com.cityflow
   ```
2. Right-click **Main.java**
3. Select **Run As** → **Java Application**

#### 5. Expected Output

**Console:**
```
=== CityFlow Traffic Simulation ===
Initializing simulation...
Created 100 intersections
All intersection threads started
Vehicle spawning started
Central Controller started
Simulation server started on port 8080
Simulation started successfully!
```

**GUI:** A maximized window will open showing the live traffic simulation with moving vehicles.

---

## 📊 Using the Application

### Main Interface Components

#### 1. **Live Traffic Map** (Left/Center)
- **10×10 grid** of intersections with roads
- **Colored backgrounds** show congestion levels:
  - 🟢 **Green** = Low traffic (0-30%)
  - 🟡 **Yellow** = Moderate traffic (30-60%)
  - 🔴 **Red** = High traffic (60-100%)
- **Traffic signals** with realistic 3-light design
- **Moving vehicles** - 40+ cars navigating the grid
- **Queue indicators** - Red circles with numbers showing waiting vehicles

#### 2. **Analytics Dashboard** (Right Panel)

**Primary Metrics:**
- **Total Vehicles** - Cumulative count of vehicles processed
- **Active Vehicles** - Cars currently on the road
- **Avg Congestion** - Average across all 100 intersections
- **Throughput** - Vehicles processed per minute

**Performance Metrics:**
- **Avg Wait Time** - Estimated average vehicle delay
- **Prediction Accuracy** - Effectiveness of forecasting algorithm
- **System Uptime** - Running time in MM:SS format

**Bottleneck Alerts:**
- **Persistent high congestion** - Intersection needs intervention
- **Rapidly increasing** - Preemptive warning
- **Critical with spillover risk** - Severe backup affecting neighbors

#### 3. **Performance Trends Chart** (Bottom)
- **Live graph** showing congestion over last 2 minutes
- **Smooth curve** updates every 0.5 seconds
- **Grid lines** for easy reading (0%, 50%, 100%)

---

## 🏗️ System Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     CityFlow Application                      │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌────────────┐  ┌────────────┐       ┌────────────┐        │
│  │Intersection│  │Intersection│  ...  │Intersection│        │
│  │ Thread 1   │  │ Thread 2   │       │ Thread 100 │        │
│  │ [Queue]    │  │ [Queue]    │       │ [Queue]    │        │
│  └─────┬──────┘  └─────┬──────┘       └─────┬──────┘        │
│        │                │                     │               │
│        └────────────────┴─────────────────────┘               │
│                         │                                     │
│                ┌────────▼──────────┐                          │
│                │ Central Controller│                          │
│                │  - Coordination   │                          │
│                │  - Adaptive Timing│◄────┐                    │
│                │  - Green Waves    │     │                    │
│                └────────┬──────────┘     │                    │
│                         │                │                    │
│                ┌────────▼──────────┐     │                    │
│                │Predictive Analyzer│     │                    │
│                │ - WMA Forecasting │     │                    │
│                │ - Bottleneck Det. │─────┘                    │
│                └────────┬──────────┘                          │
│                         │                                     │
│         ┌───────────────┼───────────────┬──────────────┐     │
│         │               │               │              │     │
│  ┌──────▼──────┐ ┌─────▼─────┐ ┌──────▼──────┐ ┌────▼────┐ │
│  │  Database   │ │  Network  │ │     GUI     │ │ Vehicle │ │
│  │   Manager   │ │  Server   │ │  (60 FPS)   │ │ Threads │ │
│  │  (SQLite)   │ │ Port 8080 │ │  Animation  │ │         │ │
│  └─────────────┘ └───────────┘ └─────────────┘ └─────────┘ │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technologies & Implementation

### Core Technologies
- **Java 17** - Latest LTS version with modern features
- **Swing** - Native GUI framework with custom painting
- **SQLite** - Lightweight embedded database (optional)
- **Docker** - Containerization for easy deployment

### Concurrency Patterns
- **Producer-Consumer** - Vehicle spawning and processing
- **Thread Pools** - ExecutorService for intersection management
- **Blocking Queues** - Thread-safe vehicle queuing
- **Synchronized Methods** - Signal state protection
- **Atomic Variables** - Counter management

### Key Algorithms

#### 1. Weighted Moving Average Prediction
```
Prediction = Σ(weight[i] × congestion_history[i])

Where:
- Recent data weighted higher (15%)
- Older data weighted lower (5%)
- 10-point rolling window
- Normalizes for incomplete history
```

#### 2. Adaptive Signal Timing
```java
if (congestion < 0.2) {
    greenDuration = 6 seconds;   // Light traffic
} else if (congestion < 0.6) {
    greenDuration = 8 seconds;   // Normal traffic
} else {
    greenDuration = 12 seconds;  // Heavy traffic
}
```

#### 3. Bottleneck Detection
```
Flags issued when:
- Congestion > 80% for 3+ cycles (Persistent)
- Congestion increasing > 20% per cycle (Rapid)
- Queue spillover to adjacent intersections (Critical)
```

---

## 📁 Project Structure

```
CityFlow-Traffic-Simulation/
├── src/
│   ├── main/java/com/cityflow/
│   │   ├── Main.java                      # Application entry point
│   │   ├── model/
│   │   │   ├── Intersection.java          # Thread managing each intersection
│   │   │   ├── Vehicle.java               # Vehicle movement logic
│   │   │   └── TrafficSignal.java         # Signal states
│   │   ├── controller/
│   │   │   ├── CentralController.java     # Coordinates all intersections
│   │   │   └── PredictiveAnalyzer.java    # Congestion forecasting
│   │   ├── database/
│   │   │   └── DatabaseManager.java       # SQLite operations
│   │   ├── network/
│   │   │   ├── SimulationServer.java      # Socket server
│   │   │   └── ClientHandler.java         # Client connection handler
│   │   └── gui/
│   │       └── OptimizedProfessionalGUI.java  # Main GUI
│   └── test/java/com/cityflow/
│       └── IntersectionTest.java          # Unit tests
├── docs/
├── Dockerfile                              # Docker container config
├── docker-compose.yml                      # Docker Compose config
├── DOCKER_SETUP.md                         # Docker documentation
├── USER_GUIDE.md                           # User manual
└── README.md                              # This file
```

---

## 🐛 Troubleshooting

### Issue: "Cannot find Main class"
**Solution:**
1. Right-click project → **Build Path** → **Configure Build Path**
2. Ensure **JRE System Library [JavaSE-17]** is listed
3. If not, click **Add Library** → **JRE System Library** → **JavaSE-17**

### Issue: Compilation errors in Eclipse
**Solution:**
1. **Project** → **Clean**
2. Delete `bin/` folder
3. **Project** → **Build Automatically** (ensure checked)

### Issue: GUI not appearing
**Solution:**
1. Check console for errors
2. Ensure Java 17+ is installed: `java -version`
3. Try: `java -Djava.awt.headless=false -cp bin com.cityflow.Main`

### Issue: "Port 8080 already in use"
**Solution:**
```bash
# Windows - Find and kill process
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill
```

---

## 🚀 Future Enhancements

- [ ] Emergency vehicles with signal priority
- [ ] Multiple vehicle types (cars, buses, trucks)
- [ ] Pedestrian crossings
- [ ] Weather conditions affecting traffic
- [ ] Machine learning predictions (LSTM)
- [ ] Real traffic data integration

---

## 📝 License

MIT License - Copyright (c) 2026 CityFlow Team

---

## 📞 Contact & Support

**Team Members:**
- **Perumal Marimuthu** - [@Perumal100](https://github.com/Perumal100)
- **Ashik John** - Database & Analytics
- **Achyuthan Sivasankar** - GUI & Testing

For issues, check [Troubleshooting](#-troubleshooting) or create a GitHub issue.

---

## 🙏 Acknowledgments

- **New York University Tandon School of Engineering** - For providing excellent education in computer science
- **Course Instructor** - For guidance on Java programming and software development
- **Java Community** - For comprehensive documentation and libraries
- **Fellow Students** - For collaboration and feedback

---

## ⭐ Show Your Support

If you find this project useful:
- ⭐ Star this repository on GitHub
- 🍴 Fork it for your own experiments
- 📢 Share it with fellow students

---
