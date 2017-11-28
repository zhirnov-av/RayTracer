package base.threads;

import base.Canvas;
import base.Scene;
import base.threads.TraceThread;

public class TraceThreadStack {


    TraceThread[] threads;

    public TraceThreadStack(int numMaxThreads) {
        threads = new TraceThread[numMaxThreads];
    }

    public TraceThread startNewThread(Scene scene, Canvas canvas, int y){
        while(true) {
            int numThreads = threads.length;
            for (int i = 0; i < numThreads; i++) {
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
