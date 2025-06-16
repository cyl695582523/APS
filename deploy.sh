#!/bin/bash

# Application name
APP_NAME="acpb-aps"
# Java options
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
# Application properties file
APP_PROPS="--spring.profiles.active=prod"
# Log directory
LOG_DIR="/var/log/$APP_NAME"
# Application directory
APP_DIR="/opt/$APP_NAME"
# JAR file name
JAR_FILE="acpb-aps-flow-master-0.0.1-SNAPSHOT.jar"

# Create necessary directories
mkdir -p $LOG_DIR
mkdir -p $APP_DIR

# Stop existing process if running
echo "Stopping existing process if running..."
PID=$(pgrep -f $JAR_FILE)
if [ ! -z "$PID" ]; then
    kill $PID
    sleep 5
    if kill -0 $PID 2>/dev/null; then
        echo "Process still running, forcing kill..."
        kill -9 $PID
    fi
fi

# Copy new JAR file
echo "Copying new JAR file..."
cp target/$JAR_FILE $APP_DIR/

# Start the application
echo "Starting application..."
nohup java $JAVA_OPTS \
    -jar $APP_DIR/$JAR_FILE \
    $APP_PROPS \
    > $LOG_DIR/app.log 2>&1 &

# Get the new process ID
NEW_PID=$!

# Wait a few seconds to check if the process is still running
sleep 5
if kill -0 $NEW_PID 2>/dev/null; then
    echo "Application started successfully with PID: $NEW_PID"
    echo "Logs are available at: $LOG_DIR/app.log"
else
    echo "Application failed to start. Check logs at: $LOG_DIR/app.log"
    exit 1
fi 