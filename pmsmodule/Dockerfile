FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file (adjust the name if needed)
COPY /target/*.jar app.jar

# Expose the application port
EXPOSE 4000

# Use wait-for-it to ensure SQL Server is ready before starting the app
ENTRYPOINT ["java", "-jar", "app.jar"]