package servent;

import app.Configuration;
import app.Logger;
import message.Message;
import message.MessageType;
import message.handler.MessageHandler;
import message.implementation.TellMessage;
import snapshot.SnapshotState;
import snapshot.SnapshotType;

import java.util.*;
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
        return new ConcurrentHashMap<>(vectorClock);
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

        if (Configuration.SNAPSHOT == SnapshotType.ALAGAR_VENKATESAN) {

            if (message.getMessageType() == MessageType.TERMINATE) {
                SnapshotState.token = Optional.empty();
            }

            if (SnapshotState.token.isPresent()) {

                if (isMessageOlderThenToken(message.getVectorClock(), SnapshotState.token.get().getVectorClock())) {

                    if (message.getMessageType() != MessageType.TELL) {
                        SnapshotState.LCS.get(message.getSender().ID()).add(message);
                    }

                }
            }
        }

        committedMessages.add(message);
        incrementClock(message.getSource().ID());
        MessageHandler.handleCommittedMessage(message);

    }

    private static boolean isMessageOlderThenToken(Map<Integer, Integer> mine, Map<Integer, Integer> other) {

        /* TODO: Prone to change. */
        /* It makes sense that because of the causal broadcast there is no need to compare vector clocks. */
        /* But, if Alagar-Venkatesan algorithm starts behaving weirdly this is the first place to look at. */

        return true;
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

                        commitMessage(pendingMessage);

                        iterator.remove();

                        break;
                    }
                }
            }
        }

    }

}