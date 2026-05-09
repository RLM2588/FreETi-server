FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml ./
RUN mvn -B -f ./pom.xml install -DskipTests
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /app
COPY --from=builder /build/services/epicapi-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]