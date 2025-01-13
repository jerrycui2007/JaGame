import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
/**
 * Transform is the class for manipulating images, such as resizing, rotating, flipping, and scaling images
 * @author Sam Ghasemzadeh and Vincent Han
 */

public class Transform {
    /**
     * Rotate an image
     * @param image the image to rotate
     * @param angle the angle to rotate the image in degrees
     * @return new rotated image
     * @author Vincent Han
     */
    public static BufferedImage rotate(BufferedImage image, double angle){
        // Get dimensions of image
        int width = image.getWidth();
        int height = image.getHeight();

        // Create new buffered image and create graphics in image
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D gd = newImage.createGraphics();

        gd.rotate(Math.toRadians(angle), width/2, height/2);
        gd.drawImage(image, 0, 0, null);
        gd.dispose();
        return newImage;
    }

    /**
     *
     * @param image
     * @param width
     * @param height
     * @return
     * @author Sam Ghasemzadeh
     */
    public static BufferedImage scale(BufferedImage image, int width, int height){
        // create a new BufferedImage with the desired width and height
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());

        // g2d is used to draw the image
        // this creates a Graphics2D, which can be used to draw into this BufferedImage https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
        Graphics2D gd = scaledImage.createGraphics();

        //returning the scaled image
        return scaledImage;

    }

    /**
     * Resizes an image by a factor
     * @param image the image to resize
     * @param widthFactor the scale factor for the width
     * @param heightFactor the scale factor for the height
     * @return the resized image
     * @author Vincent Han
     */
    public static BufferedImage scaleByFactor(BufferedImage image, int widthFactor, int heightFactor){
        // Set new dimensions
        int width = image.getWidth() * widthFactor;
        int height = image.getHeight() * heightFactor;

        // create a new BufferedImage with the desired width and height
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());

        // g2d is used to draw the image
        // this creates a Graphics2D, which can be used to draw into this BufferedImage https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
        Graphics2D gd = scaledImage.createGraphics();
        gd.drawImage(image, width, height, null);
        gd.dispose();
        return scaledImage;
    }

//Sam
//    public static BufferedImage flipHorizontal(BufferedImage image){
//
//    }
//Sam
//    public static BufferedImage flipVertical(BufferedImage image){
//
//    }
//Vincent
//    public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height){
//
//    }

    /**
     *
     * @param image
     * @return
     * @author Sam Ghasemzadeh
     */
    public static BufferedImage greyscale(BufferedImage image){
        // create a new BufferedImage that is grayscale, by setting thetyrp to image.TYPE_BYTE_GRAY
        BufferedImage grayScaleImage = new BufferedImage(image.getWidth(), image.getHeight(), image.TYPE_BYTE_GRAY);
        // g2d is used to draw the image
        // this creates a Graphics2D, which can be used to draw into this BufferedImage
        Graphics2D gd = grayScaleImage.createGraphics();

        // drawing the new image
        gd.drawImage(grayScaleImage, grayScaleImage.getWidth(), grayScaleImage.getHeight(), null);
        //disposing the g2d before returning
        gd.dispose();

        return grayScaleImage;
    }
}

