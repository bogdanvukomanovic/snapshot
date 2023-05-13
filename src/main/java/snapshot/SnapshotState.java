package snapshot;

import message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SnapshotState {

    public static Map<Integer, Integer> GSS;
    public static Map<Integer, List<Message>> LCS;
    public static Optional<Message> token = Optional.empty();

    public static Map<Integer, Integer> getGSS() {
        return GSS;
    }

    public static Map<Integer, List<Message>> getLCS() {
        return LCS;
    }

    public static Optional<Message> getToken() {
        return token;
    }

    public static void initializeGSS() {
        GSS = new ConcurrentHashMap<>();
    }

    public static void resetGlobalState() {
        GSS = new ConcurrentHashMap<>();
    }

    public static void initializeLCS(List<Integer> neighbours) {

        LCS = new ConcurrentHashMap<>();

        for (Integer neighbour : neighbours) {
            LCS.put(neighbour, new ArrayList<>());
        }

    }

}
