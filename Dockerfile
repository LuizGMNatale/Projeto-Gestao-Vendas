FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/gestao-vendas-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_APPLICATION_NAME=gestao-vendas

ENTRYPOINT ["java", "-jar", "app.jar"]
