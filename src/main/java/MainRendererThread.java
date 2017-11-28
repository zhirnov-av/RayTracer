import base.Canvas;
import base.Scene;

public class MainRendererThread extends Thread {

    private Canvas canvas;
    private Scene scene;

    public MainRendererThread(Canvas canvas, Scene scene) {
        this.canvas = canvas;
        this.scene = scene;
    }

    @Override
    public void run(){
        long start = System.currentTimeMillis();
        canvas.fillCanvasV2(scene);
        long end = System.currentTimeMillis();
        System.out.println(String.format("getIntersection = %d isPointInV2 = %d traceRay = %d fillCanvas = %d", scene.times.get("getIntersection"), scene.times.get("isPointInV2"), scene.times.get("traceRay"), end - start));
    }
}
