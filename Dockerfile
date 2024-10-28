FROM node:20 AS frontend
WORKDIR /app
COPY vue-frontend/package*.json ./
RUN npm install
COPY vue-frontend .
RUN npm run build

FROM gradle:jdk-21-and-22 AS build
WORKDIR /app
COPY . .
COPY --from=frontend /app/dist src/main/resources/static
RUN gradle bootJar

FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
