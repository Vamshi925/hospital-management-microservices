# ---------- Stage 1 : Build the Application ----------
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the Maven configuration first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Give execute permission to the Maven wrapper
RUN chmod +x mvnw

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the application source code
COPY src src

# Build the executable JAR
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2 : Runtime ----------
FROM eclipse-temurin:21-jre

# Create the application directory
WORKDIR /app

# Copy only the generated JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Eureka runs on port 8761
EXPOSE 8761

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
