package com.example;
import com.rabbitmq.client.*;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class InitRabbitMQ {

    private static final String EXCHANGE_NAME = "images_exchange";

    public static void main(String[] args) throws Exception {
        // Conectar no RabbitMQ (host = nome do serviço no docker-compose)
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); // esse é o container definido no docker-compose
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declarar um exchange do tipo "topic"
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);

            // Criar filas
            String faceQueue = "face_queue";
            String teamQueue = "team_queue";

            channel.queueDeclare(faceQueue, true, false, false, null);
            channel.queueDeclare(teamQueue, true, false, false, null);

            // Vincular filas ao exchange com routing keys
            channel.queueBind(faceQueue, EXCHANGE_NAME, "face.*");
            channel.queueBind(teamQueue, EXCHANGE_NAME, "team.*");

            System.out.println("RabbitMQ inicializado com Exchange + Filas:");
            System.out.println("- Exchange: " + EXCHANGE_NAME);
            System.out.println("- Queue (Face): " + faceQueue + " (routing key: face.*)");
            System.out.println("- Queue (Team): " + teamQueue + " (routing key: team.*)");
        }
    }
}
