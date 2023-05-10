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
    private static List<Message> committedMessages = new CopyOnWriteArrayList<>();
    private static Queue<Message> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();

    public static void initializeVectorClock(int serventCount) {

        for (int i = 0; i < serventCount; i++) {
            vectorClock.put(i, 0);
        }

    }

    public static Map<Integer, Integer> copyVectorClock() {

        Map<Integer, Integer> copy = new ConcurrentHashMap<>();

        for (Map.Entry<Integer, Integer> entry : History.getVectorClock().entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }

        return copy;
    }

    public static void incrementClock(int ID) {
        vectorClock.computeIfPresent(ID, (key, oldValue) -> oldValue + 1);
    }

    public static Map<Integer, Integer> getVectorClock() {
        return vectorClock;
    }

    public static List<Message> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static void addPendingMessage(Message message) {
        pendingMessages.add(message);
    }

    public static void commitMessage(Message message) {

        committedMessages.add(message);
        incrementClock(message.getSource().ID()); /* TODO: getSender() or getSource()? */

        /* TODO: Message (ASK, TELL, TRANSACTION...) can be handled only upon committing. */

        checkPendingMessages();
    }

    private static boolean isOtherClockGreater(Map<Integer, Integer> mine, Map<Integer, Integer> other) {

        for (int i = 0; i < mine.size(); i++) {
            if (other.get(i) > mine.get(i)) {
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

                    if (!isOtherClockGreater(myVectorClock, pendingMessage.getVectorClock())) {

                        gotWork = true;

                        Logger.timestampedStandardPrint("Committing " + pendingMessage);

                        committedMessages.add(pendingMessage);
                        incrementClock(pendingMessage.getSource().ID()); /* TODO: getSender() or getSource()? */

                        Logger.newLineBarrierPrint(vectorClock.toString());

                        iterator.remove();

                        break;
                    }
                }
            }
        }

    }

}