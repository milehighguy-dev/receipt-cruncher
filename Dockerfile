# Build the JAR
FROM maven:3.9.2-eclipse-temurin-8 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Run the app
FROM openjdk:8-jdk-alpine

LABEL author="Ben Raymond"

WORKDIR /app

COPY --from=build /app/target/receipt-cruncher-1.0.0.jar receipt-cruncher-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "receipt-cruncher-1.0.0.jar"]
