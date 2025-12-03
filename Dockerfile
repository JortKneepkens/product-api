# --- Build Stage ---
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy Maven wrapper and config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (faster subsequent builds)
RUN ./mvnw -q dependency:go-offline

# Copy source
COPY src ./src

# Build the application
RUN ./mvnw -q package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
