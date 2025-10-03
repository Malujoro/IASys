package com.consumer;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;
import java.io.File;

public class TeamConsumer {
    private final static String EXCHANGE_NAME = "images";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq"); // nome do container RabbitMQ
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            // Só consome mensagens com routing key "team"
            channel.queueBind(queueName, EXCHANGE_NAME, "team");

            TeamModel model = new TeamModel("src/main/resources/model_team.bin");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                byte[] body = delivery.getBody();
                // String com.consumer.TeamModel.predict(File imageFile) throws Exception
                File file = new File("src/main/resources/dataset_times/" + new String(body, StandardCharsets.UTF_8));

                try {
                    String result = model.predict(file);
                    System.out.println("[Time Detectado] " + result);
                } catch (Exception e) {
                    System.err.println("Error predicting team: " + e.getMessage());
                    e.printStackTrace();
                }
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }
}
