package snapshot;

import message.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SnapshotState {

    public static Map<Integer, Integer> sent;
    public static Map<Integer, Integer> received;

    public static Map<Integer, Snap> GSS;
    public static Map<Integer, List<Message>> LCS;
    public static Optional<Message> token = Optional.empty();

    public static Map<Integer, Snap> getGSS() {
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

    public static void initializeSent(Integer serventCount) {
        sent = new HashMap<>();
        for (int i = 0; i < serventCount; i++) {
            sent.put(i, 0);
        }
    }

    public static void initializeReceived(Integer serventCount) {
        received = new HashMap<>();
        for (int i = 0; i < serventCount; i++) {
            received.put(i, 0);
        }
    }

    public static Map<Integer, Integer> copySent() {
        return new ConcurrentHashMap<>(sent);
    }

    public static Map<Integer, Integer> copyReceived() {
        return new ConcurrentHashMap<>(received);
    }

}
