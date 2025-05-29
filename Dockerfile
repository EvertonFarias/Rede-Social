# Etapa de build
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

# Gera o JAR com todas as dependências (fat jar)
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o JAR gerado para o container final
COPY --from=build /app/target/auth-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
