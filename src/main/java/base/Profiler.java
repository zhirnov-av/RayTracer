package base;

import java.util.HashMap;

public class Profiler {
    public static HashMap<String, ProfilerItem> times = new HashMap<>();
    private static long time = 0;

    private Profiler() {
    }

    public static void init(){
        time = System.currentTimeMillis();
    }

    public static void check(String methodName){
        time = System.currentTimeMillis() - time;
        ProfilerItem item = times.get(methodName);
        if (item != null){
            item.countUsing++;
            item.summaryTime += time;
        }else{
            item = new ProfilerItem();
            item.countUsing = 1;
            item.summaryTime = time;
            times.put(methodName, item);
        }
    }

}
