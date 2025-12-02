FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven wrapper and config
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# -- Runtime Stage --
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
