# Use the official OpenJDK 21 image as a base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file (or source code) to the container's working directory
# If you are building a JAR file from your local project, change 'your-app.jar' accordingly
COPY . .


RUN javac -cp ".:./lib/pdfbox-app-3.0.3.jar" SimpleHttpServer.java PDFHighlighter.java StringMatcher.java

# Run the JAR file with Java 21
CMD ["java", "-cp", ".:./lib/pdfbox-app-3.0.3.jar", "SimpleHttpServer"]
