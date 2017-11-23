import exceptions.RayTracerException;

public class TraceThreadStack {
    TraceThread[] threads;
    public TraceThreadStack(int maxThreads){
        threads = new TraceThread[maxThreads];
    }
    public TraceThread startNewThread(Scene scene, Canvas canvas, int y){
        while(true) {
            for (int i = 0; i < threads.length; i++) {
                if (threads[i] == null) {
                    threads[i] = new TraceThread(scene, canvas, y);
                    return threads[i];
                } else {
                    if (!threads[i].isAlive()) {
                        threads[i] = new TraceThread(scene, canvas, y);
                        return threads[i];
                    }
                }
            }
        }
    }
}
