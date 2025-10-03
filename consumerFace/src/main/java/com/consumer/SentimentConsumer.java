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
    private final static String QUEUE_NAME = "fila_face";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = null;

        // Loop de retry até o RabbitMQ estar disponível
        while (connection == null) {
            try {
                connection = factory.newConnection();
            } catch (Exception e) {
                System.out.println("RabbitMQ ainda não disponível, tentando novamente em 3s...");
                Thread.sleep(3000);
            }
        }

        Channel channel = connection.createChannel();

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

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        System.out.println("Consumidor pronto, aguardando mensagens na fila 'face'...");
    }
}
