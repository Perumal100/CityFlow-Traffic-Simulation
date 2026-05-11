# 🐳 Docker Setup for CityFlow

This guide shows you how to run CityFlow using Docker - **no Java or Eclipse installation required!**

---

## 🎯 Prerequisites

### Install Docker Desktop

**Windows/Mac:**
1. Download Docker Desktop: https://www.docker.com/products/docker-desktop
2. Install and start Docker Desktop
3. Ensure it's running (icon in system tray)

**Linux:**
```bash
sudo apt-get update
sudo apt-get install docker.io docker-compose
sudo systemctl start docker
sudo systemctl enable docker
```

---

## 🚀 Quick Start (3 Steps)

### **Option 1: Using Docker Compose (Easiest)**

```bash
# 1. Clone the repository
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation

# 2. Build and run
docker-compose up --build
```

That's it! The GUI will appear.

---

### **Option 2: Using Docker Commands**

```bash
# 1. Clone repository
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation

# 2. Build Docker image
docker build -t cityflow:latest .

# 3. Run the container
docker run -it --rm \
  -e DISPLAY=host.docker.internal:0 \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  --network host \
  cityflow:latest
```

---

## 🖥️ Platform-Specific Setup

### **Windows**

1. **Install Docker Desktop**
2. **Install VcXsrv (X Server for Windows)**
   - Download: https://sourceforge.net/projects/vcxsrv/
   - Install and run XLaunch
   - Select: "Multiple windows", Display 0
   - Select: "Start no client"
   - **IMPORTANT:** Check "Disable access control"
   - Click "Finish"

3. **Run Docker container:**
```bash
docker-compose up --build
```

### **Mac**

1. **Install Docker Desktop**
2. **Install XQuartz (X Server for Mac)**
   ```bash
   brew install --cask xquartz
   ```

3. **Configure XQuartz:**
   - Start XQuartz
   - Go to Preferences → Security
   - Enable "Allow connections from network clients"
   - Restart XQuartz

4. **Allow Docker to connect:**
   ```bash
   xhost +localhost
   ```

5. **Run Docker container:**
   ```bash
   docker-compose up --build
   ```

### **Linux**

1. **Install Docker**
   ```bash
   sudo apt-get install docker.io docker-compose
   ```

2. **Allow Docker to access display:**
   ```bash
   xhost +local:docker
   ```

3. **Run Docker container:**
   ```bash
   docker-compose up --build
   ```

---

## 📦 What Docker Does

```
1. Downloads Java 17 base image
2. Installs required GUI libraries
3. Copies your CityFlow source code
4. Compiles all Java files
5. Runs the application
6. Shows GUI on your screen
```

**Result:** One-command deployment!

---

## 🛑 Stopping the Simulation

Press `Ctrl+C` in the terminal where Docker is running.

Or:
```bash
docker-compose down
```

---

## 🔧 Troubleshooting

### **Issue: GUI doesn't appear**

**Windows:**
- Make sure VcXsrv is running
- Check "Disable access control" is enabled in XLaunch

**Mac:**
- Ensure XQuartz is running
- Run: `xhost +localhost`

**Linux:**
- Run: `xhost +local:docker`

### **Issue: "Cannot connect to X server"**

**Solution:**
```bash
# Linux/Mac
export DISPLAY=:0
xhost +local:docker

# Then run docker-compose again
docker-compose up
```

### **Issue: Port 8080 already in use**

**Solution:**
```bash
# Find what's using port 8080
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux

# Stop that process or change CityFlow port in Main.java
```

---

## 🎨 Advanced: Custom Configuration

### **Change Number of Vehicles**

Edit `Dockerfile` and add environment variable:
```dockerfile
ENV MAX_VEHICLES=60
```

### **Run in Background**

```bash
docker-compose up -d
```

### **View Logs**

```bash
docker-compose logs -f
```

### **Rebuild After Code Changes**

```bash
docker-compose up --build
```

---

## 📊 Docker vs Traditional Setup

| Feature | Traditional | Docker |
|---------|------------|--------|
| Java Installation | Required | Not needed |
| Eclipse | Required | Not needed |
| Setup Time | 15-20 minutes | 2 minutes |
| Commands | 5+ steps | 1 command |
| Cross-platform | Issues | Works everywhere |
| Dependencies | Manual | Automatic |

---

## 🌟 For GitHub Users

When you share your repository, users can run it with:

```bash
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation
docker-compose up
```

**That's it!** No Java, no Eclipse, no configuration!

---

## 🎓 For Professors/Reviewers

Add this to your project submission:

**"To run the project instantly without any setup:**
```bash
docker-compose up --build
```
**Full Docker instructions in DOCKER_SETUP.md"**

---

## 📝 Notes

- First build takes 2-3 minutes (downloads Java image)
- Subsequent runs are instant
- Docker image size: ~500MB
- Container can be shared as a complete package

---

## 🚀 Next Steps

1. Test the Docker setup locally
2. Push Dockerfile to GitHub
3. Add Docker badge to README:
   ```markdown
   ![Docker](https://img.shields.io/badge/Docker-Enabled-blue?logo=docker)
   ```

---

**Docker makes your project instantly runnable anywhere!** 🐳
