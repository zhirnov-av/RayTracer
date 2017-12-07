package base;

import java.util.HashMap;

public class Profiler {
    public static HashMap<String, ProfilerItem> times = new HashMap<>();
    private long time = 0;

    public Profiler(){
        time = System.currentTimeMillis();
    }


    public void check(String methodName){
        long tm = System.currentTimeMillis() - time;
        ProfilerItem item = times.get(methodName);
        if (item != null){
            item.countUsing++;
            item.summaryTime += tm;
        }else{
            item = new ProfilerItem();
            item.countUsing = 1;
            item.summaryTime = tm;
            times.put(methodName, item);
        }
    }

}
