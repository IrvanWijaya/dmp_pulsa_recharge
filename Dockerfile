FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

ARG SERVICE_DIR

WORKDIR /app
COPY ["$SERVICE_DIR", "."]

RUN ls -al /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]