FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/complaint-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]