import java.awt.*;

/**
 * Text is the class that draws and customizes text
 */

public class Text {
    private int x;
    private int y;
    private String text;
    private Font font;
    private Color color;

    public Text(String text, int x, int y, Font font, Color color){
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.font = font;
    }

    // Getters and setters
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }
    public Font getFont(){
        return font;
    }
    public void setFont(Font font){
        this.font = font;
    }
    public Color getColor(){
        return color;
    }
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Draws text on the screen
     * @param g
     */
    public void draw(Graphics g){
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

}
