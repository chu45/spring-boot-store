FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
EXPOSE 8080
COPY target/store-1.0.0.jar storeapp.jar
ENTRYPOINT ["java", "-jar", "storeapp.jar"]