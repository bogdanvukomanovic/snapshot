package message.handler;

import app.Logger;
import message.Message;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Handler {

    private static ExecutorService service = Executors.newWorkStealingPool();
    private static Set<Message> received = Collections.newSetFromMap(new ConcurrentHashMap<Message, Boolean>());

    public static void handleReceivedMessage(Message message) {

        if (!received.add(message)) {
            Logger.timestampedStandardPrint("Already had this. Rejecting.");
            return;
        }

        switch (message.getMessageType()) {

            case BROADCAST:
                service.submit(ReceivedMessage.BROADCAST(message));
                break;

        }

    }

    public static void handleCommittedMessage(Message message) {

        /* ... */

    }

}
