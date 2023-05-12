package snapshot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class State {

    public static Map<Integer, Integer> GSS = new ConcurrentHashMap<>();

    public static Map<Integer, Integer> getGlobalServentState() {
        return GSS;
    }

    /* TODO: Not sure if I need this. */
    public static void resetGlobalState() {
        GSS = new ConcurrentHashMap<>();
    }

}
