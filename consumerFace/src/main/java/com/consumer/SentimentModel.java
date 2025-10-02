package com.consumer;

import smile.classification.SVM;
import smile.io.Serialization;
import java.io.File;

public class SentimentModel {

    private SVM<double[]> model;

    public SentimentModel(String path) throws Exception {
        this.model = Serialization.read(path);
    }

    public String predict(File imageFile) throws Exception {
        double[] vector = ImageUtils.imageToVector(imageFile, 28, 28);
        int label = model.predict(vector);
        return label == 1 ? "Feliz 😃" : "Triste 😢";
    }
}
