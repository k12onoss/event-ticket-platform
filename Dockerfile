# ====== Build stage ======
FROM maven:3.9.11-eclipse-temurin-24 AS build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -Pproduction

# ====== Runtime stage ======
FROM eclipse-temurin:24.0.2_12-jre
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
