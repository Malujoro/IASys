package com.consumer;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

public class SentimentConsumer {
    private final static String EXCHANGE_NAME = "images";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); // nome do container RabbitMQ
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            // Só consome mensagens com routing key "face"
            channel.queueBind(queueName, EXCHANGE_NAME, "face");

            SentimentModel model = new SentimentModel("src/main/resources/model_sentiment.bin");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                byte[] body = delivery.getBody();
                String result = model.predict(body);
                System.out.println("[Sentimento Detectado] " + result);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }
}
