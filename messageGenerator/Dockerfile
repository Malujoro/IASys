FROM openjdk:17-jdk-slim

WORKDIR /app

# cria diretórios de saída e imagens
RUN mkdir -p /app/out /app/src /app/images

# comando padrão: compila e roda seu código usando o volume
CMD ["sh", "-c", "javac /app/src/main/java/com/example/MessageGenerator.java -d /app/out && java -cp /app/out com.example.MessageGenerator"]
