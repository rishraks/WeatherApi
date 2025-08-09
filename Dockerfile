FROM eclipse-temurin:21-jdk-alpine

# Copy everything into the container
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build the jar inside the container
RUN ./mvnw clean package -DskipTests

# Run the jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
