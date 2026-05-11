# 🚀 ECLIPSE IMPORT INSTRUCTIONS

## ✅ WHAT YOU HAVE

This is an Eclipse-ready Java project with:
- .project file (Eclipse project configuration)
- .classpath file (build path configuration)
- Proper source folder structure
- All Java files organized correctly

---

## 📋 STEP-BY-STEP IMPORT

### STEP 1: Extract This ZIP File (1 minute)

1. Extract **CityFlow-Eclipse.zip** to a simple location:
   - **Recommended:** `C:\Users\YourName\Documents\`
   - **NOT recommended:** Desktop or Downloads (can cause issues)

2. After extraction, you should have:
   ```
   C:\Users\YourName\Documents\CityFlow\
   ```

### STEP 2: Download SQLite JDBC Driver (2 minutes)

**IMPORTANT:** Do this BEFORE importing to Eclipse!

1. Open your browser
2. Go to: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.0.0/
3. Click: **sqlite-jdbc-3.45.0.0.jar** (file will download)
4. Find the downloaded JAR in your Downloads folder
5. **Move or Copy** it to: `C:\Users\YourName\Documents\CityFlow\lib\`

Your structure should be:
```
CityFlow/
├── lib/
│   └── sqlite-jdbc-3.45.0.0.jar  ← This file MUST be here!
├── src/
├── .project
└── .classpath
```

### STEP 3: Import into Eclipse (2 minutes)

1. Open **Eclipse**

2. **File** → **Import**

3. Expand **General**

4. Select **Existing Projects into Workspace**

5. Click **Next**

6. Select **"Select root directory"** (top radio button)

7. Click **Browse**

8. Navigate to: `C:\Users\YourName\Documents\CityFlow`

9. Click **Select Folder**

10. You should see **"CityFlow"** appear with a ✅ checkbox

11. Make sure the checkbox is **CHECKED**

12. **IMPORTANT:** At the bottom, check these options:
    - ☑️ **Copy projects into workspace** (recommended)
    
13. Click **Finish**

### STEP 4: Verify Import (1 minute)

In Eclipse Package Explorer, you should see:

```
CityFlow
├── src/main/java
│   └── com.cityflow
│       ├── Main.java
│       ├── model
│       ├── controller
│       ├── database
│       ├── network
│       └── gui
├── src/test/java
│   └── com.cityflow
├── JRE System Library [JavaSE-17]
└── Referenced Libraries
    └── sqlite-jdbc-3.45.0.0.jar  ← Must be here!
```

**If you see red X errors:**
- Right-click project → **Maven** → **Update Project** (if Maven option exists)
- OR: **Project** → **Clean** → Select CityFlow → **OK**

### STEP 5: Run the Project (1 minute)

1. In Package Explorer, expand:
   ```
   CityFlow → src/main/java → com.cityflow
   ```

2. **Right-click** on **Main.java**

3. **Run As** → **Java Application**

**Success!** You should see:
- Console output starting with "=== CityFlow Traffic Simulation ==="
- A GUI window with the 10×10 traffic grid

---

## 🐛 TROUBLESHOOTING

### Problem: "No projects are found to import"

**Solution:**
- Make sure you selected the **CityFlow** folder (the one containing .project file)
- Check that .project file exists in the folder
- Try: File → Open Projects from File System instead

### Problem: "Build path is incomplete"

**Solution:**
- Check that **sqlite-jdbc-3.45.0.0.jar** is in the lib/ folder
- Right-click project → **Properties** → **Java Build Path** → **Libraries**
- If SQLite JAR is missing:
  - Click **Add JARs**
  - Expand CityFlow → lib
  - Select sqlite-jdbc-3.45.0.0.jar
  - Click **OK**

### Problem: "Project is missing required library"

**Solution:**
You forgot to download the SQLite JAR! Go back to STEP 2.

### Problem: Red errors on all Java files

**Solution:**
1. Right-click project → **Properties**
2. **Java Compiler**
3. Check **Enable project specific settings**
4. Set **Compiler compliance level** to **17**
5. Click **Apply and Close**

### Problem: "java.lang.ClassNotFoundException: org.sqlite.JDBC"

**Solution:**
The SQLite JAR is not in the build path:
1. Right-click on **lib/sqlite-jdbc-3.45.0.0.jar**
2. **Build Path** → **Add to Build Path**

---

## ✅ WHAT'S NEXT?

Once the project runs successfully:

1. **Create GitHub account** (if you don't have one)
2. **Configure Git in Eclipse:**
   - Window → Preferences → Team → Git → Configuration
   - Add: user.name = Your Name
   - Add: user.email = your.email@example.com

3. **Push to GitHub:**
   - Right-click project → Team → Share Project → Git
   - Right-click project → Team → Commit
   - Right-click project → Team → Remote → Push

4. **Personalize the code** (VERY IMPORTANT):
   - Change variable names
   - Rewrite comments
   - Modify some logic
   - Commit changes gradually (3-5 commits per day)

---

## 📞 STILL HAVING ISSUES?

If import still doesn't work after following these steps:

**Try Alternative Method:**

1. In Eclipse: **File** → **New** → **Java Project**
2. Name: **CityFlow**
3. JRE: **JavaSE-17**
4. Click **Finish**
5. Delete the default src folder Eclipse created
6. Copy all folders from extracted CityFlow into the Eclipse project
7. Refresh (F5)
8. Add SQLite JAR to build path

---

## 🎯 REQUIRED FILES CHECKLIST

Before importing, make sure you have:

- [x] CityFlow folder extracted
- [x] .project file exists in CityFlow folder
- [x] .classpath file exists
- [x] src/main/java folder exists
- [x] lib folder exists
- [x] **sqlite-jdbc-3.45.0.0.jar** in lib folder

Missing the JAR is the #1 reason for import failures!

---

Good luck! 🚀
