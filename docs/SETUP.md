# CityFlow Setup Guide

This guide will walk you through setting up CityFlow from scratch, including all dependencies and GitHub configuration.

## Table of Contents
1. [Prerequisites Installation](#prerequisites-installation)
2. [Project Setup](#project-setup)
3. [GitHub Configuration](#github-configuration)
4. [Building the Project](#building-the-project)
5. [Running the Simulation](#running-the-simulation)
6. [Common Issues](#common-issues)

---

## Prerequisites Installation

### Step 1: Install Java JDK 17+

#### Windows:
1. Download from: https://adoptium.net/temurin/releases/
2. Choose: Windows x64, JDK 17 or higher, .msi installer
3. Run installer, accept defaults
4. Verify installation:
   ```cmd
   java -version
   javac -version
   ```

#### macOS:
1. Download from: https://adoptium.net/temurin/releases/
2. Choose: macOS, JDK 17 or higher, .pkg installer
3. Install and verify:
   ```bash
   java -version
   javac -version
   ```

#### Linux:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
javac -version
```

### Step 2: Install Git

#### Windows:
1. Download: https://git-scm.com/download/win
2. Run installer, use recommended settings
3. Verify: `git --version`

#### macOS:
```bash
# If you have Homebrew:
brew install git

# Or download from:
# https://git-scm.com/download/mac
```

#### Linux:
```bash
sudo apt install git
git --version
```

### Step 3: Choose an IDE (Optional but Recommended)

**Option A: IntelliJ IDEA Community (Recommended)**
1. Download: https://www.jetbrains.com/idea/download/
2. Choose Community Edition (free)
3. Install and launch

**Option B: Eclipse**
1. Download: https://www.eclipse.org/downloads/
2. Choose "Eclipse IDE for Java Developers"

**Option C: VS Code**
1. Download: https://code.visualstudio.com/
2. Install "Extension Pack for Java"

---

## Project Setup

### Step 1: Clone or Download the Project

If you received this as a ZIP file:
```bash
# Extract the ZIP file to a folder, then:
cd CityFlow
```

If cloning from GitHub:
```bash
git clone https://github.com/YOUR_USERNAME/CityFlow-Traffic-Simulation.git
cd CityFlow-Traffic-Simulation
```

### Step 2: Download SQLite JDBC Driver

**Automatic (recommended):**

Linux/Mac:
```bash
mkdir -p lib
curl -L -o lib/sqlite-jdbc-3.45.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
```

Windows (PowerShell):
```powershell
mkdir lib -Force
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/sqlite-jdbc-3.45.0.0.jar" -OutFile "lib/sqlite-jdbc-3.45.0.0.jar"
```

**Manual:**
1. Go to: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/
2. Download: `sqlite-jdbc-3.45.0.0.jar`
3. Create a `lib` folder in your project directory
4. Place the JAR file inside the `lib` folder

### Step 3: Verify Project Structure

Your folder should look like this:
```
CityFlow/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── cityflow/
│   └── test/
├── lib/
│   └── sqlite-jdbc-3.45.0.0.jar
├── .gitignore
└── README.md
```

---

## GitHub Configuration

### Step 1: Create GitHub Account
1. Go to: https://github.com
2. Sign up for a free account
3. Verify your email

### Step 2: Configure Git Locally

```bash
# Set your name (use your real name)
git config --global user.name "Your Name"

# Set your email (use your GitHub email)
git config --global user.email "your.email@example.com"

# Verify settings
git config --list
```

### Step 3: Create GitHub Repository

1. Go to: https://github.com/new
2. Repository name: `CityFlow-Traffic-Simulation`
3. Description: `Real-time traffic simulation with predictive analytics - CS6103 Final Project`
4. Visibility: **Public**
5. **DO NOT** check "Initialize with README" (you already have files)
6. Click "Create repository"

### Step 4: Connect Local Repository to GitHub

```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# First commit
git commit -m "Initial commit: CityFlow traffic simulation project"

# Add GitHub as remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/CityFlow-Traffic-Simulation.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Step 5: Verify Upload
1. Go to your GitHub repository URL
2. Refresh the page
3. You should see all your files

---

## Building the Project

### Method 1: Command Line (Works on all systems)

**Linux/Mac:**
```bash
# Create bin directory
mkdir -p bin

# Compile all Java files
javac -d bin -cp "lib/*" $(find src/main/java -name "*.java")

# If you get errors, try:
find src/main/java -name "*.java" > sources.txt
javac -d bin -cp "lib/*" @sources.txt
```

**Windows (Command Prompt):**
```cmd
:: Create bin directory
mkdir bin

:: Compile
javac -d bin -cp "lib/*" src/main/java/com/cityflow/*.java src/main/java/com/cityflow/model/*.java src/main/java/com/cityflow/controller/*.java src/main/java/com/cityflow/database/*.java src/main/java/com/cityflow/network/*.java src/main/java/com/cityflow/gui/*.java
```

### Method 2: Using an IDE

**IntelliJ IDEA:**
1. File → Open → Select CityFlow folder
2. Right-click `lib/sqlite-jdbc-3.45.0.0.jar` → Add as Library
3. Right-click `src/main/java` → Mark Directory as → Sources Root
4. Build → Build Project

**Eclipse:**
1. File → Open Projects from File System → Select CityFlow folder
2. Right-click project → Build Path → Add External Archives → Select SQLite JAR
3. Project → Build All

**VS Code:**
1. File → Open Folder → Select CityFlow
2. The Java extension should detect the project
3. Add `lib/*.jar` to classpath in `.vscode/settings.json`:
```json
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

---

## Running the Simulation

### Method 1: Command Line

**Linux/Mac:**
```bash
java -cp "bin:lib/*" com.cityflow.Main
```

**Windows:**
```cmd
java -cp "bin;lib/*" com.cityflow.Main
```

### Method 2: Using IDE

**IntelliJ IDEA / Eclipse:**
1. Navigate to `src/main/java/com/cityflow/Main.java`
2. Right-click on the file
3. Select "Run 'Main.main()'"

**VS Code:**
1. Open `Main.java`
2. Click "Run" button above the `main` method
3. Or press F5

### Expected Output

When running successfully, you should see:
```
=== CityFlow Traffic Simulation ===
Initializing simulation...
Database initialized
Created 100 intersections
All intersection threads started
Vehicle spawning started
Central Controller started
Simulation server started on port 8080
Simulation started successfully!
- Grid size: 10x10
- Total intersections: 100
- Server port: 8080
```

And a GUI window showing the traffic grid.

---

## Common Issues

### Issue: "Error: Could not find or load main class"

**Solution:**
```bash
# Make sure you're in the project root directory
pwd  # or 'cd' on Windows

# Verify bin folder exists and has compiled classes
ls bin/com/cityflow/  # or 'dir' on Windows

# Recompile
javac -d bin -cp "lib/*" src/main/java/com/cityflow/**/*.java
```

### Issue: "java.lang.ClassNotFoundException: org.sqlite.JDBC"

**Solution:**
- Verify `lib/sqlite-jdbc-3.45.0.0.jar` exists
- Check your classpath includes `lib/*`
- Try using absolute path: `java -cp "bin:/full/path/to/lib/*" com.cityflow.Main`

### Issue: "error: package org.junit.jupiter.api does not exist"

**Solution:**
JUnit is only needed for tests. You can run the main application without it.

To add JUnit for testing:
```bash
curl -L -o lib/junit-platform-console-standalone-1.9.3.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar
```

### Issue: Port 8080 already in use

**Solution:**
1. Find what's using port 8080:
   ```bash
   # Linux/Mac:
   lsof -i :8080
   
   # Windows:
   netstat -ano | findstr :8080
   ```
2. Either stop that application, or change the port in `Main.java`:
   ```java
   private static final int SERVER_PORT = 8081; // Use different port
   ```

### Issue: Git push asks for password repeatedly

**Solution:**
Use Personal Access Token (PAT):
1. Go to GitHub → Settings → Developer Settings → Personal Access Tokens
2. Generate new token (classic)
3. Give it `repo` scope
4. Copy the token
5. When pushing, use token as password

Or use SSH:
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your.email@example.com"

# Add to GitHub: Settings → SSH Keys → New SSH Key
# Paste contents of: ~/.ssh/id_ed25519.pub

# Change remote URL
git remote set-url origin git@github.com:YOUR_USERNAME/CityFlow-Traffic-Simulation.git
```

---

## Next Steps

1. **Make it yours**: Customize colors, timing, grid size
2. **Add features**: Implement the enhancements from feedback
3. **Test thoroughly**: Run multiple times, check for crashes
4. **Document changes**: Update README with your modifications
5. **Commit regularly**: 
   ```bash
   git add .
   git commit -m "Add feature: XYZ"
   git push
   ```

## Need Help?

- Review the main README.md
- Check Java documentation: https://docs.oracle.com/en/java/
- SQLite JDBC: https://github.com/xerial/sqlite-jdbc
- Git basics: https://git-scm.com/book/en/v2

---

**Remember**: Work on this incrementally. Don't try to do everything at once. Build, test, commit, repeat!
