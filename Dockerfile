FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-23.0.1-jdk -y
RUN apt-get install maven -y

COPY . .
RUN mvn clean install
RUN mvn package -DskipTests

FROM openjdk:23.0.1-jdk
EXPOSE 8080

COPY --from=build /target/auth-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
