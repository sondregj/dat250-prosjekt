FROM gradle:jdk-21-and-22 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
