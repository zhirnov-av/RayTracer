package base;

import java.util.HashMap;

public class Profiler {
    public static HashMap<String, ProfilerItem> times = new HashMap<>();
    public static HashMap<String, Long> initialTime = new HashMap<>();
    //private static long time = 0;

    private Profiler() {
    }

    public static void init(String methodName){
        //if (!Constants.USE_PROFILER)
        //    return;
        initialTime.put(methodName, System.currentTimeMillis());
    }

    public static void check(String methodName){
        //if (!Constants.USE_PROFILER)
        //    return;
        long time = System.currentTimeMillis() - initialTime.get(methodName);
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
