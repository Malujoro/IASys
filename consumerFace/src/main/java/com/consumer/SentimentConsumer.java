package com.consumer;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

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

            SentimentModel model = new SentimentModel("/app/src/main/resources/model_sentiment.bin");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    byte[] body = delivery.getBody();

                    // Decodifica Base64 enviado pelo gerador
                    String base64 = new String(body);
                    byte[] imageBytes = Base64.getDecoder().decode(base64);

                    // Converte para BufferedImage
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    String prediction = model.predict(img);
                    System.out.println("[Sentimento Detectado] " + prediction);
                }  catch (Exception e) {
                    System.err.println("Erro ao processar a imagem: " + e.getMessage());
                    e.printStackTrace();
                }
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        }
    }
}
