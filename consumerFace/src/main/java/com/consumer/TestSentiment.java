package com.consumer;

import java.io.File;

public class TestSentiment {
    public static void main(String[] args) throws Exception {
        SentimentModel model = new SentimentModel("src/main/resources/model_sentiment.bin");

        File feliz = new File("src/main/resources/dataset_sentimento/feliz1.jpg");
        File triste = new File("src/main/resources/dataset_sentimento/triste1.jpg");

        System.out.println("feliz1.jpg -> " + model.predict(feliz));
        System.out.println("triste1.jpg -> " + model.predict(triste));
    }
}
