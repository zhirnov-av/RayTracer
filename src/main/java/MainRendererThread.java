import base.*;

public class MainRendererThread extends Thread {

    private Canvas canvas;
    private Scene scene;

    public MainRendererThread(Canvas canvas, Scene scene) {
        this.canvas = canvas;
        this.scene = scene;
    }

    @Override
    public void run(){

        scene.prepareTracing(canvas);
        if (Constants.USE_MULTITHREADING) {
            Profiler.init("fillCanvasV2");
            canvas.fillCanvasV2(scene);
            Profiler.check("fillCanvasV2");
        }else{
            Profiler.init("fillCanvas");
            canvas.fillCanvas(scene);
            Profiler.check("fillCanvas");
        }

        for(String key: Profiler.times.keySet()){
            ProfilerItem item = Profiler.times.get(key);
            System.out.println(String.format("%s: time: %d  count: %d", key, item.summaryTime, item.countUsing));
        }

        System.out.println("Done!");

    }
}
