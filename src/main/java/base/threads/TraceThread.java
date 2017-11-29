package base.threads;

import base.Canvas;
import base.Color;
import base.Scene;

public class TraceThread extends Thread {
    private Canvas canvas;
    private Scene scene;
    private int y;
    public TraceThread(Scene scene, Canvas canvas, int y){
        this.canvas = canvas;
        this.scene = scene;
        this.y = y;
    }

    @Override
    public void run(){
        for(int i = 0; i < canvas.getWidth(); i++){
            //Long l = scene.times.get("traceRay");
            //if ( l == null ) l = 0L;

            int x = i - canvas.getWidth() / 2;
            //long start = System.currentTimeMillis();
            Color cl = scene.traceRayV2(canvas, x, y);
            //l += (System.currentTimeMillis() - start);

            //scene.times.put("traceRay", l);

            if (cl == null)
                cl = new Color(0, 0, 0);
            canvas.putPixel(x, y, cl);
        }
    }
}
