package message.handler;

import app.Logger;
import message.Message;

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
            Logger.emptyLine();
            return;
        }

        service.submit(ReceivedMessage.RECEIVE(message));

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
            case TERMINATE:
                service.submit(CommittedMessage.TERMINATE(message));
                break;

        }

    }

}
