# Etapa 1: Construcción del proyecto
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=docker"]
