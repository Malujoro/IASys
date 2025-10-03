package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.util.Random;

import java.nio.file.Files;
import java.util.Base64;

public class MessageGenerator {
    private static final Random random = new Random();
    private static final File[] rostos;
    private static final File[] times;

    private static final String QUEUE_NAME = "mensagens";

    static {
        rostos = new File("/app/images/pessoas").listFiles();
        times = new File("/app/images/times").listFiles();
    }

    public static void main(String[] args) throws Exception {
        // Conectar no RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); // nome do container RabbitMQ no docker-compose
        factory.setUsername("guest");
        factory.setPassword("guest");

        while(true) {
            try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
                String exchangeName = "images";
                channel.exchangeDeclare(exchangeName, "topic");

                int messagesPerSecond = 5;
                long delay = 1000 / messagesPerSecond;

                while (true) {
                    String message = generateMessage();

                    String routingKey;
                    if (message.contains("feliz") || message.contains("triste")) {
                        routingKey = "face";  // é rosto de pessoa
                    } else {
                        routingKey = "team";  // é brasão de time
                    }

                    channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));

                    Thread.sleep(delay);
                }
            } catch (Exception e) {
                System.out.println("RabbitMQ não disponível ainda...");
                Thread.sleep(3000);
            }
        }
    }

    private static String generateMessage() throws Exception {
        boolean tipoPessoa = random.nextBoolean();
        File imagemArquivo;

        if (tipoPessoa) {
            imagemArquivo = rostos[random.nextInt(rostos.length)];
            System.out.println("Tipo: Rosto de pessoa | Imagem: " + imagemArquivo.getName() + " | Timestamp: " + System.currentTimeMillis());
        } else {
            imagemArquivo = times[random.nextInt(times.length)];
            System.out.println("Tipo: Brasão de time | Imagem: " + imagemArquivo.getName() + " | Timestamp: " + System.currentTimeMillis());
        }

        byte[] bytes = Files.readAllBytes(imagemArquivo.toPath());
        String base64 = Base64.getEncoder().encodeToString(bytes);

        return base64;
    }
}
