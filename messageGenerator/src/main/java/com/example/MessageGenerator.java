package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.util.Random;

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
                System.out.println("[ENVIADO] " + message);

                Thread.sleep(delay);
            }
        }
    }

    private static String generateMessage() {
        boolean tipoPessoa = random.nextBoolean();
        String imagemEscolhida;

        if (tipoPessoa) {
            imagemEscolhida = rostos[random.nextInt(rostos.length)].getName();
            return "Tipo: Rosto de pessoa | Imagem: " + imagemEscolhida + " | Timestamp: " + System.currentTimeMillis();
        } else {
            imagemEscolhida = times[random.nextInt(times.length)].getName();
            return "Tipo: Brasão de time | Imagem: " + imagemEscolhida + " | Timestamp: " + System.currentTimeMillis();
        }
    }
}
