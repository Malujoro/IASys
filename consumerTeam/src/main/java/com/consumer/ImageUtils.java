package com.consumer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    // reduz a imagem para 32x32 e converte em vetor de double
    public static double[] extractFeatures(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        BufferedImage resized = new BufferedImage(32, 32, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, 32, 32, null);
        g.dispose();

        double[] features = new double[32 * 32];
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int rgb = resized.getRGB(x, y);
                int gray = rgb & 0xFF;
                features[y * 32 + x] = gray / 255.0; // normalizar
            }
        }
        return features;
    }
}
