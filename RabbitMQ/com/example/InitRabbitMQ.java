package com.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class InitRabbitMQ {

    private static final String EXCHANGE_NAME = "image_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Exchange do tipo topic
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            // Fila de rostos
            channel.queueDeclare("fila_rostos", false, false, false, null);
            channel.queueBind("fila_rostos", EXCHANGE_NAME, "image.face.*");

            // Fila de times
            channel.queueDeclare("fila_times", false, false, false, null);
            channel.queueBind("fila_times", EXCHANGE_NAME, "image.team.*");

            System.out.println("Exchange e filas criadas com sucesso!");
        }
    }
}
