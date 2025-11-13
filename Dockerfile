FROM maven:3.9-eclipse-temurin-23-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package
FROM eclipse-temurin:23-jre-alpine AS final
WORKDIR /app
RUN apk add --no-cache \
    fontconfig \
    ttf-dejavu \
    libxtst \
    libxi
COPY --from=builder /app/target/CurrencyConverter-Challenge-ONE-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
RUN mkdir /app/config
CMD ["java", "-cp", "/app/config/:/app/app.jar", "org.example.Main"]