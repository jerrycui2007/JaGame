import java.awt.geom.AffineTransform;
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

        // Create new buffered image and create graphics to draw the image
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D gd = newImage.createGraphics();

        gd.rotate(Math.toRadians(angle), width/2, height/2);
        gd.drawImage(image, 0, 0, null);
        gd.dispose();
        return newImage;
    }

    /**
     * This method scales the image into a new image with the given dimensions.
     * @param image The given Image
     * @param width The new width
     * @param height The new height
     * @return the scaled image.
     * @author Sam Ghasemzadeh
     */
    public static BufferedImage scale(BufferedImage image, int width, int height){
        // create a new BufferedImage with the desired width and height
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());

        // g2d is used to draw the image
        // this creates a Graphics2D, which can be used to draw into this BufferedImage https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
        Graphics2D gd = scaledImage.createGraphics();
        gd.drawImage(image, 0, 0, null);

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
    public static BufferedImage scaleByFactor(BufferedImage image, double widthFactor, double heightFactor){
        // Set new dimensions
        int width = (int)(image.getWidth() * widthFactor);
        int height = (int)(image.getHeight() * heightFactor);

        // create a new BufferedImage with the desired width and height
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());

        // g2d is used to draw the image
        // this creates a Graphics2D, which can be used to draw into this BufferedImage https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html
        Graphics2D gd = scaledImage.createGraphics();
        gd.drawImage(image, width, height, null);
        gd.dispose();
        return scaledImage;
    }


    /**
     * Flip an image horizontally
     * @param image the image to flip
     * @return new image
     * @author Vincent Han
     */
    public static BufferedImage flipHorizontal(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D gd = flippedImage.createGraphics();

        // Reverse the image along the x-axis
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        // Translate the image back to be visible (width distance to the right, width is negative because it got reversed)
        transform.translate(-width, 0);

        gd.drawImage(image, transform, null);
        gd.dispose();
        return flippedImage;
    }

    /**
     * Flip an image vertically
     * @param image the image to flip
     * @return new image
     * @author Vincent Han
     */
    public static BufferedImage flipVertical(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D gd = flippedImage.createGraphics();

        // Reverse the image along the y-axis
        AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
        // Translate the image back to be visible (height distance down, height is negative because it got reversed)
        transform.translate(0, -height);

        gd.drawImage(image, transform, null);
        gd.dispose();
        return flippedImage;
    }

    /**
     * Crops a rectangular region from an image
     * @param image the image to crop
     * @param x the x starting point of the new image
     * @param y the y starting point of the new image
     * @param width the width of the new image
     * @param height the height of the new image
     * @return the cropped image
     */
    public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height){
        return image.getSubimage(x, y, width, height);
    }

    /**
     * This method converts an image into graysacle.
     * @param image the input image.
     * @return the graysacle image.
     * @author Sam Ghasemzadeh
     */
    public static BufferedImage grayscale(BufferedImage image){
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

