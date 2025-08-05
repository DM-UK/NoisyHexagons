package noisyhexagons.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage loadImage(String str) {
        try {
            return ImageIO.read(new File(str));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Color darkern(Color color, double  factor) {
        int r = (int)(color.getRed() * factor);
        int g = (int)(color.getGreen() * factor);
        int b = (int)(color.getBlue() * factor);

        return new Color(r, g, b);
    }
}
