package com.consumer;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;
import smile.util.ModelSerializer;

import java.io.*;
import java.util.*;

public class TrainSentimentModel {
    public static void main(String[] args) throws Exception {
        String datasetPath = "src/main/resources/dataset_sentimento/";
        String labelsFile = datasetPath + "labels.csv";

        List<double[]> featuresList = new ArrayList<>();
        List<Integer> labelsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(labelsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String filename = parts[0];
                int label = Integer.parseInt(parts[1]);

                double[] features = ImageUtils.extractFeatures(new File(datasetPath + filename));
                featuresList.add(features);
                labelsList.add(label);
            }
        }

        double[][] features = featuresList.toArray(new double[0][]);
        int[] labels = labelsList.stream().mapToInt(i -> i).toArray();

        // Treinar SVM binário (feliz=1, triste=0)
        SVM<double[]> svm = new SVM<>(new LinearKernel(), 2.0, 2);
        svm.learn(features, labels);
        svm.finish();

        ModelSerializer.save(svm, "src/main/resources/model_sentiment.bin");
        System.out.println("Modelo de sentimento treinado e salvo em model_sentiment.bin");
    }
}
