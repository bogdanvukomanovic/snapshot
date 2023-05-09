package servent;

import app.Logger;
import message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class History {

    private static Map<Integer, Integer> vectorClock = new ConcurrentHashMap<>();
    private static List<Message> committedCausalMessageList = new CopyOnWriteArrayList<>();
    private static Queue<Message> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();

    public static void initializeVectorClock(int serventCount) {

        for (int i = 0; i < serventCount; i++) {
            vectorClock.put(i, 0);
        }

    }

    public static void incrementClock(int ID) {
        vectorClock.computeIfPresent(ID, (key, oldValue) -> oldValue + 1);
    }

    public static Map<Integer, Integer> getVectorClock() {
        return vectorClock;
    }

    public static List<Message> getCommittedCausalMessageList() {
        return new CopyOnWriteArrayList<>(committedCausalMessageList);
    }

    public static void addPendingMessage(Message message) {
        pendingMessages.add(message);
    }

    public static void commitCausalMessage(Message message) {
        committedCausalMessageList.add(message);
        incrementClock(message.getSender().ID());

        checkPendingMessages();
    }

    private static boolean otherClockGreater(Map<Integer, Integer> clock1, Map<Integer, Integer> clock2) {

        for (int i = 0; i < clock1.size(); i++) {
            if (clock2.get(i) > clock1.get(i)) {
                return true;
            }
        }

        return false;
    }

    public static void checkPendingMessages() {

        boolean gotWork = true;

        while (gotWork) {

            gotWork = false;

            synchronized (pendingMessagesLock) {

                Iterator<Message> iterator = pendingMessages.iterator();

                Map<Integer, Integer> myVectorClock = getVectorClock();

                while (iterator.hasNext()) {

                    Message pendingMessage = iterator.next();

                    if (!otherClockGreater(myVectorClock, pendingMessage.getVectorClock())) {

                        gotWork = true;

                        Logger.timestampedStandardPrint("Committing " + pendingMessage);

                        committedCausalMessageList.add(pendingMessage);
                        incrementClock(pendingMessage.getSender().ID());

                        iterator.remove();

                        break;
                    }
                }
            }
        }

    }

}