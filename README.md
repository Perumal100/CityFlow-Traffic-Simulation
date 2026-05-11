# 🚦 CityFlow Traffic Simulation System

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Active-success)
![Threads](https://img.shields.io/badge/Threads-100+-blue)

Real-time multi-threaded traffic simulation with predictive analytics, adaptive signal control, and professional GUI visualization.

---

## 👥 Project Team

This project was developed as part of **CS-UY 1114 - Introduction to Java**

**Team Members:**
- **Perumal Marimuthu** - [@Perumal100](https://github.com/Perumal100) - Lead Developer & System Architecture
- **Ashik John** - Database Design & Predictive Analytics
- **Achyuthan Sivasankar** - GUI Development & Testing

**Institution:** New York University Tandon School of Engineering  
**Semester:** Spring 2026  
**Course:** CS-UY 1114 - Introduction to Java

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
