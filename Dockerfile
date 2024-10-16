FROM gradle:8.0.2-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
