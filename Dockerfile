FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9090
CMD ["java", "-jar", "app.jar"]
