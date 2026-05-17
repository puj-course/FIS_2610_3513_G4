FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests --no-transfer-progress

FROM eclipse-temurin:21-jre-jammy

LABEL org.opencontainers.image.title="Fashtoll Backend" \
      org.opencontainers.image.description="Spring Boot REST API para la plataforma Fashtoll" \
      org.opencontainers.image.vendor="Ceiba" \
      org.opencontainers.image.source="https://github.com/nicojoya/FIS_2610_3513_G4"

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -r fashtoll && useradd -r -g fashtoll -s /sbin/nologin fashtoll

COPY --from=build /app/target/*.jar app.jar
RUN chown fashtoll:fashtoll app.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/fashtoll_db \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    JAVA_OPTS=""

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

USER fashtoll

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
