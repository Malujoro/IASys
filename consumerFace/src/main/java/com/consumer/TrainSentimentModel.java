package com.consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;

public class TrainSentimentModel {

    public static void main(String[] args) throws Exception {
        File datasetDir = new File("src/main/resources/dataset_sentimento");
        File[] files = datasetDir.listFiles();

        List<double[]> featuresList = new ArrayList<>();
        List<Integer> labelsList = new ArrayList<>();

        for (File file : files) {
            double[] vector = ImageUtils.imageToVector(file, 28, 28); // tamanho fixo 28x28
            featuresList.add(vector);

            if (file.getName().toLowerCase().contains("feliz")) {
                labelsList.add(1);
            } else {
                labelsList.add(0); // triste
            }
        }

        double[][] X = featuresList.toArray(new double[0][]);
        int[] y = labelsList.stream().mapToInt(i -> i).toArray();

        // Treina SVM
        double C = 1.0;
        SVM<double[]> svm = SVM.fit(X, y, new LinearKernel(), C, 1e-3);

        // Salva o modelo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/model_sentiment.bin"))) {
            oos.writeObject(svm);
        }

        System.out.println("Modelo treinado e salvo com sucesso!");
    }
}

