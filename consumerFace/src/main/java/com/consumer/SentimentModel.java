package com.consumer;

import smile.classification.SVM;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SentimentModel {

    private SVM<double[]> model;

    public SentimentModel(String path) throws Exception {
        this.model = (SVM<double[]>) loadModel(path);
    }

    public String predict(BufferedImage imageFile) throws Exception {
        double[] vector = ImageUtils.imageToVector(imageFile, 28, 28);
        int label = model.predict(vector);
        return label == 1 ? "Feliz 😃" : "Triste 😢";
    }

    public static Object loadModel(String modelPath) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath))) {
            return ois.readObject();
        }
    }
}
