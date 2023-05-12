package message.handler;

import app.Logger;
import message.Message;
import snapshot.TransactionManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {

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
            case TRANSACTION:
                service.submit(ReceivedMessage.TRANSACTION(message));
                break;
            case ASK:
                service.submit(ReceivedMessage.ASK(message));
                break;
            case TELL:
                service.submit(ReceivedMessage.TELL(message));
                break;

        }

    }

    public static void handleCommittedMessage(Message message) {

        switch (message.getMessageType()) {

            case TRANSACTION:
                service.submit(CommittedMessage.TRANSACTION(message));
                break;
            case ASK:
                service.submit(CommittedMessage.ASK(message));
                break;
            case TELL:
                service.submit(CommittedMessage.TELL(message));
                break;

        }

    }

}
