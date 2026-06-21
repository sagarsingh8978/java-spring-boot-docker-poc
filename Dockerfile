# Step 1: Start with a lightweight Java 21 runtime environment
FROM eclipse-temurin:21-jre-alpine
# Step 2: Set a working directory inside the container
WORKDIR /app
# Step 3: Copy your compiled JAR from your local target folder into the container
COPY build/libs/*-SNAPSHOT.jar app-executor.jar
# Step 4: Command to execute the application when the container starts
ENTRYPOINT ["java","-jar","app-executor.jar"]

