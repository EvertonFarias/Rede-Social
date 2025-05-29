# Etapa de build com Maven + JDK prontos
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# Etapa final só com JDK (imagem mais leve)
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/auth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
