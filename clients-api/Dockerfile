# Use official OpenJDK 24 image as base
FROM eclipse-temurin:24-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven build artifact
COPY target/clients-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
