package com.example;

import java.io.File;
import java.util.Random;

public class MessageGenerator {
    private static final Random random = new Random();
    private static final File[] rostos;
    private static final File[] times;

    static {
        rostos = new File("/app/images/pessoas").listFiles();
        times = new File("/app/images/times").listFiles();
    }

    public static void main(String[] args) throws InterruptedException {
        int messagesPerSecond = 5;
        long delay = 1000 / messagesPerSecond;

        while (true) {
            String message = generateMessage();
            System.out.println("[MSG] " + message);
            Thread.sleep(delay);
        }
    }

    private static String generateMessage() {
        // Escolhe tipo aleatório
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
