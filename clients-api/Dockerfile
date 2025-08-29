# Stage 1: building
FROM maven:3.9-eclipse-temurin-24-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: running
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/clients-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
