FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/cloud-store-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]