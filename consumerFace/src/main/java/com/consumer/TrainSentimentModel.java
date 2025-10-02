package com.consumer;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;
import smile.io.Serialization;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        SVM<double[]> svm = SVM.fit(X, y, new LinearKernel(), 1.0);

        // Salva o modelo
        Serialization.write(svm, "src/main/resources/model_sentiment.bin");

        System.out.println("Modelo treinado e salvo com sucesso!");
    }
}
