FROM gradle:8.4-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_APPLICATION_NAME=HomeRepUserService
ENV SERVER_PORT=8083

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
