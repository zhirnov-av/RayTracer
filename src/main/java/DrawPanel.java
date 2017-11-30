import base.Canvas;
import base.Color;
import base.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel {
    private base.Canvas canvas;
    private Scene scene;

    public DrawPanel(Canvas canvas, Scene scene){
        super();
        this.canvas = canvas;
        this.scene = scene;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);



        /*
        Long start = System.currentTimeMillis();
        BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < canvas.getWidth(); x++){
            for (int y = 0; y < canvas.getHeight(); y++){
                try {
                    Color cl = canvas.getBitmap()[x][y];
                    if (cl.getR() > 255) cl.setR(255);
                    if (cl.getG() > 255) cl.setG(255);
                    if (cl.getB() > 255) cl.setB(255);
                    int col = (cl.getR() << 16) | (cl.getG() << 8) | cl.getB();
                    img.setRGB(x, y, col);
                }catch (Exception e){
                }
            }
        }
        */

        g.drawImage(canvas.getBitmap(), 0, 0, null);

        /*
        Long time = System.currentTimeMillis() - start;

        Long tm = scene.times.get("paintComponent");
        if (tm == null)
            tm = new Long(time);
        else
            tm += time;
        scene.times.put("paintComponent", tm);
        */

    }
}