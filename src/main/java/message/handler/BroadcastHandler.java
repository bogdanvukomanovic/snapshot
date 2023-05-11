package message.handler;

import app.Configuration;
import app.Logger;
import message.Message;
import message.util.MessageUtil;
import servent.History;
import servent.Servent;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastHandler implements MessageHandler {

    private final Message message;

    private static Set<Message> received = Collections.newSetFromMap(new ConcurrentHashMap<Message, Boolean>());

    public BroadcastHandler(Message message) {
        this.message = message;
    }

    @Override
    public void run() {

        Servent source = message.getSource();
        Servent sender = message.getSender();

        if (source.ID() == Configuration.SERVENT.ID()) {
            Logger.timestampedStandardPrint("Got own message back. No rebroadcast.");
            return;
        }

        if (received.add(message)) {

            Logger.timestampedStandardPrint("Added to pending " + message.getBody() + " from " + sender + " originally broadcast by " + source);

            History.addPendingMessage(message);
            History.checkPendingMessages(); /* TODO: Maybe move after rebroadcasting. */

            for (Integer neighbour : Configuration.SERVENT.neighbours()) {

                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);

                MessageUtil.sendMessage(message.makeMeASender()
                                               .changeReceiver(neighbour));

            }

        } else {
            Logger.timestampedStandardPrint("Already had this. No rebroadcast.");
        }

    }

}
