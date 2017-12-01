import base.Canvas;
import base.Profiler;
import base.ProfilerItem;
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
        scene.prepareTracing(canvas);
        canvas.fillCanvasV2(scene);
        long end = System.currentTimeMillis();
        System.out.println(String.format("paintComponent = %d isPointInV2 = %d traceRay = %d fillCanvas = %d", scene.times.get("paintComponent"), scene.times.get("isPointInV2"), scene.times.get("traceRay"), end - start));

        for(String key: Profiler.times.keySet()){
            ProfilerItem item = Profiler.times.get(key);
            System.out.println(String.format("%s: time: %d  count: %d", key, item.summaryTime, item.countUsing));
        }
    }
}
