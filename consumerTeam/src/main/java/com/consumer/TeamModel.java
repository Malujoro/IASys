package com.consumer;

import smile.classification.SVM;
import smile.util.ModelSerializer;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class TeamModel {
    private SVM<double[]> classifier;

    public TeamModel(String modelPath) {
        try {
            classifier = ModelSerializer.load(new java.io.File(modelPath));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar modelo: " + e.getMessage(), e);
        }
    }

    public String predict(byte[] imageBytes) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            BufferedImage resized = new BufferedImage(32, 32, BufferedImage.TYPE_BYTE_GRAY);

            Graphics2D g = resized.createGraphics();
            g.drawImage(img, 0, 0, 32, 32, null);
            g.dispose();

            double[] features = new double[32 * 32];
            for (int y = 0; y < 32; y++) {
                for (int x = 0; x < 32; x++) {
                    int rgb = resized.getRGB(x, y);
                    int gray = rgb & 0xFF;
                    features[y * 32 + x] = gray / 255.0;
                }
            }

            int label = classifier.predict(features);

            // Mapeamento fictício planeta->time
            String[] teams = {"MercurioFC", "VenusFC", "TerraFC", "MarteFC", "JupiterFC", "SaturnoFC", "UranoFC", "NetunoFC"};
            return teams[label];
        } catch (Exception e) {
            return "Erro ao classificar: " + e.getMessage();
        }
    }
}
