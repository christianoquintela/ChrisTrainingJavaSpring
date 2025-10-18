FROM openjdk:17-jdk-slim

RUN mkdir /app

WORKDIR /app

EXPOSE 8080

COPY /target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

