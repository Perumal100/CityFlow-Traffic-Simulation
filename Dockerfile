# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Install X11 libraries for GUI support
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    x11-apps \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY src ./src
COPY .classpath .
COPY .project .

# Create bin directory
RUN mkdir -p bin

# Compile the project
RUN javac -d bin -sourcepath src/main/java \
    $(find src/main/java -name "*.java")

# Set display for GUI
ENV DISPLAY=host.docker.internal:0

# Run the application
CMD ["java", "-cp", "bin", "com.cityflow.Main"]
