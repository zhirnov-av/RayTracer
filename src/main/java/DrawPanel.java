import base.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel {
    private Canvas canvas;

    public DrawPanel(Canvas canvas){
        super();
        this.canvas = canvas;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < canvas.getWidth(); x++){
            for (int y = 0; y < canvas.getHeight(); y++){
                try {
                    Color cl = canvas.getBitmap()[x][y];
                    int col = (cl.getR() << 16) | (cl.getG() << 8) | cl.getB();
                    img.setRGB(x, y, col);
                }catch (Exception e){
                }
            }
        }


        g.drawImage(img, 0, 0, null);

    }
}