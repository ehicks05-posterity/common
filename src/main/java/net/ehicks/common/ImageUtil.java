package net.ehicks.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil
{
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;

        // Create a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

        // Draw the image on to the buffered image
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bufferedImage;
    }

    public static BufferedImage getScaledImage(BufferedImage image, int newWidth)
    {
        double scalingFactor = newWidth / (double)image.getWidth();
        int scaledWidth = (int) (scalingFactor * image.getWidth());
        int scaledHeight = (int) (scalingFactor * image.getHeight());
        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return toBufferedImage(scaledImage);
    }

    public static String toBase64String(BufferedImage img) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", out);
        byte[] bytes = out.toByteArray();

        String base64bytes = Base64.getEncoder().encodeToString(bytes);
        return "data:image/png;base64," + base64bytes;
    }
}
