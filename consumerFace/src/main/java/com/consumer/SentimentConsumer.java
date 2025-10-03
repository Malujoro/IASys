package com.consumer;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;

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

            SVM<double[]> model = (SVM<double[]>) SentimentModel.loadModel("sentiment.model");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                byte[] body = delivery.getBody();
                // Aqui você precisa converter 'body' para o formato de características esperado pelo modelo
                double[] features = convertToFeatures(body);
                int prediction = model.predict(features);
                System.out.println("[Sentimento Detectado] " + prediction);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }

    private static double[] convertToFeatures(byte[] body) {
        // Implemente a conversão de byte[] para double[] aqui
        // Isso depende de como seus dados estão estruturados
        return new double[0];
    }
}
