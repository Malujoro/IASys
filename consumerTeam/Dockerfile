# Consumidor 2 - Times
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o jar gerado pelo Maven
COPY target/consumerTeam-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Diretório de recursos (dataset + modelo)
COPY src/main/resources/ src/main/resources/

CMD ["java", "-jar", "app.jar"]
