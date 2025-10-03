package com.consumer;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static double[] imageToVector(File file, int width, int height) throws Exception {
        BufferedImage img = ImageIO.read(file);
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        scaled.getGraphics().drawImage(img, 0, 0, width, height, null);

        double[] vector = new double[width * height];
        int idx = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = scaled.getRGB(x, y) & 0xFF; // grayscale
                vector[idx++] = pixel / 255.0; // normaliza entre 0 e 1
            }
        }
        return vector;
    }
}
