FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven

COPY . .
RUN mvn clean install -DskipTests
RUN mvn package -DskipTests

FROM openjdk:21-jdk
EXPOSE 8080

COPY --from=build /target/auth-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
