package message.handler;

import app.Configuration;
import message.Message;
import message.implementation.BroadcastMessage;
import message.util.Mailbox;
import servent.History;

public class RequestMessage {

    public static Thread BROADCAST(Message message) {

        return new Thread(() -> {

            History.commitMessage(message);
            History.checkPendingMessages();

            for (Integer neighbor : Configuration.SERVENT.neighbours()) {
                Mailbox.sendMessage(message.changeReceiver(neighbor));
            }

        });

    }

}
