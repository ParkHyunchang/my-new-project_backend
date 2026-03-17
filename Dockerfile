FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/new-project-api-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/logs
EXPOSE 3210
ENTRYPOINT ["java", "-jar", "app.jar"]
