# Docker Setup Section - Add this to your README.md

## 🐳 Quick Start with Docker (Recommended)

**Run the entire simulation with one command - no Java or Eclipse needed!**

### Prerequisites
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop)

### Run in 30 Seconds

```bash
# Clone the repository
git clone https://github.com/Perumal100/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation

# Run with Docker Compose
docker-compose up --build
```

**That's it!** The GUI will appear and simulation will start.

### Windows Users: Additional Step

Install VcXsrv (X Server):
1. Download: https://sourceforge.net/projects/vcxsrv/
2. Run XLaunch → Multiple windows → Display 0
3. ✓ Check "Disable access control"
4. Then run: `docker-compose up --build`

### Full Docker Documentation

See [DOCKER_SETUP.md](DOCKER_SETUP.md) for detailed instructions, troubleshooting, and platform-specific setup.

---

## 🖥️ Traditional Setup (Without Docker)

If you prefer to run without Docker:

### Prerequisites
- Java 17 or higher
- Eclipse IDE

### Steps
1. Download ZIP or clone repository
2. Import to Eclipse (File → Import → Existing Projects)
3. Project → Clean
4. Run `Main.java`

See full instructions in [Installation](#installation) section below.

---
