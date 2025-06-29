# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set work directory
WORKDIR /app

# Copy project files
COPY pom.xml ./
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# -------- Stage 2: Run --------
FROM eclipse-temurin:21-jre

# Create app directory
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Copy maps directory
COPY maps /app/maps

# Set environment variable for profile (can be overridden in docker-compose)
ENV SPRING_PROFILES_ACTIVE=prod

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
