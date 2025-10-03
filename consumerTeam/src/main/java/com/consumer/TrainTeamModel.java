package com.consumer;

import smile.classification.SVM;
import smile.math.kernel.LinearKernel;

import java.io.*;
import java.util.*;

public class TrainTeamModel {
    public static void main(String[] args) throws Exception {
        String datasetPath = "src/main/resources/dataset_times/";
        String labelsFile = datasetPath + "labels.csv";

        List<double[]> featuresList = new ArrayList<>();
        List<Integer> labelsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(labelsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String filename = parts[0];
                int label = Integer.parseInt(parts[1]);

                double[] features = ImageUtils.imageToVector(new File(datasetPath + filename), 28, 28);
                featuresList.add(features);
                labelsList.add(label);
            }
        }

        double[][] features = featuresList.toArray(new double[0][]);
        int[] labels = labelsList.stream().mapToInt(i -> i).toArray();

        double C = 2.0;
        int numClasses = 8;
        SVM<double[]> svm = SVM.fit(features, labels, new LinearKernel(), C, 1e-3);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/model_team.bin"))) {
            oos.writeObject(svm);
        }
        System.out.println("Modelo treinado e salvo em model_team.bin");
    }
}
