import base.Color;
import exceptions.RayTracerException;

public class Canvas {
    private int height = 0;
    private int width = 0;
    private int halfWidth = 0;
    private int halfHeight = 0;

    private Color[][] bitmap;


    public Canvas(int width, int height) {
        this.height = height;
        this.width = width;
        this.halfWidth = this.width / 2;
        this.halfHeight = this.height / 2;

        this.bitmap = new Color[this.width][this.height];
    }

    public void putPixel(int x, int y, Color color){
        int convX = halfWidth + x;
        int convY = halfHeight - y;
        if (convX >= 0 && convY >= 0 && convX < this.width && convY < this.height)
            bitmap[convX][convY] = color;
    }

    public Color getPixel(int x, int y) throws RayTracerException {
        int convX = x + halfWidth;
        int convY = y + halfHeight;
        if (convX > 0 && convY > 0 && convX < this.width && convY < this.height)
            return bitmap[convX][convY];
        else
            throw new RayTracerException("X or Y is out if bounds");
    }

    public void fillCanvas(Scene scene){
        for (int x = -width/2; x < width/2; x++){
            for (int y = -height/2; y < height/2; y++){

                Long l = scene.times.get("traceRay");
                if ( l == null ) l = 0L;

                long start = System.currentTimeMillis();
                Color cl = scene.traceRay(this, x, y);
                l += (System.currentTimeMillis() - start);

                scene.times.put("traceRay", l);

                if (cl == null)
                    cl = new Color(0, 0, 0);
                putPixel(x, y, cl);
            }
        }
    }

    public void fillCanvasV2(Scene scene){

        TraceThreadStack threadStack = new TraceThreadStack(8);
        TraceThread thread;
        for (int y = -height/2; y < height/2; y++){
            thread = threadStack.startNewThread(scene, this, y);
            thread.start();
        }
    }


    public Color[][] getBitmap() {
        return bitmap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
