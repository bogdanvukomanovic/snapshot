package message.handler;

import app.Configuration;
import app.Logger;
import message.Message;
import message.util.MessageUtil;
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

        Logger.timestampedStandardPrint("Got " + message.getBody() + " from " + sender + " originally broadcast by " + source);

        if (source.ID() == Configuration.SERVENT.ID()) {
            Logger.timestampedStandardPrint("Got own message back. No rebroadcast.");
        } else {

            if (received.add(message)) {

                for (Integer neighbour : Configuration.SERVENT.neighbours()) {
                    Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
                    MessageUtil.sendMessage(message.changeReceiver(neighbour).makeMeASender());
                }

            } else {
                Logger.timestampedStandardPrint("Already had this. No rebroadcast.");
            }

        }

    }

}
