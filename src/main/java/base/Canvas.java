package base;

import base.Color;
import base.Scene;
import base.threads.TraceThread;
import base.threads.TraceThreadStack;
import exceptions.RayTracerException;
import tree.TreeNode;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.util.ArrayList;

public class Canvas {
    private int height = 0;
    private int width = 0;
    private int halfWidth = 0;
    private int halfHeight = 0;

    private BufferedImage bitmap;

    //private Color[][] bitmap;


    public Canvas(int width, int height) {
        this.height = height;
        this.width = width;
        this.halfWidth = this.width / 2;
        this.halfHeight = this.height / 2;

        this.bitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //this.bitmap = new Color[this.width][this.height];
    }

    public void putPixel(int x, int y, Color color){
        int convX = halfWidth + x;
        int convY = halfHeight - y - 1;

//        int r = (color.getR()|255)&255;
//        int g = (color.getG()|255)&255;
//        int b = (color.getB()|255)&255;
        int r = color.getR();
        int g = color.getG();
        int b = color.getB();
        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;

        int col = (r << 16) | (g << 8) | b;

        bitmap.setRGB(convX, convY, col);
    }

    /*
    public Color getPixel(int x, int y) throws RayTracerException {
        int convX = x + halfWidth;
        int convY = y + halfHeight;
        if (convX > 0 && convY > 0 && convX < this.width && convY < this.height)
            return bitmap[convX][convY];
        else
            throw new RayTracerException("X or Y is out if bounds");
    }
    */



    public void fillCanvasV3(Scene scene){
        ArrayList<TreeNode> nodes = null;
        for (int x = -width/2; x < width/2; x++){
            for (int y = -height/2; y < height/2; y++){
                //nodes = scene.fillListNodes(this, x, y, scene.bBoxes.getRoot(), nodes);
                //Color cl = scene.traceRay(this, x, y);
            }
        }
    }

    public void fillCanvasV2(Scene scene){

        TraceThreadStack threadStack = new TraceThreadStack(8);
        TraceThread thread;
        int startY = -height/2;
        int endY = height/2;
        for (int y = startY; y < endY; y++){
            thread = threadStack.startNewThread(scene, this, y);
            thread.start();
        }
    }

    public void fillCanvas(Scene scene){
        for (int x = -width/2; x < width/2; x++){
            for (int y = -height/2; y < height/2; y++){

                Color cl = scene.traceRayV2(x, y, 0);
                //Color cl = scene.traceRayV2(x, y);

                if (cl == null)
                    cl = new Color(0, 0, 0);
                putPixel(x, y, cl);
            }
        }
    }

    public BufferedImage getBitmap() {
        return bitmap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
