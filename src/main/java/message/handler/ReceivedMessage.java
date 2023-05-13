package message.handler;

import app.Configuration;
import app.Logger;
import message.Message;
import message.util.Mailbox;
import servent.History;

public class ReceivedMessage {

    /* TODO:
        - It looks like we always need to perform same set of actions upon receiving message.
        - It is likely that this is going to be refactored.
    */

    public static Runnable RECEIVE(Message message) {

        return new Thread(() -> {

            if (message.getSource().ID() == Configuration.SERVENT.ID()) {
                Logger.timestampedStandardPrint("Got own "+ message.getMessageType() + " message back. No rebroadcast.");
                return;
            }

            // Logger.timestampedStandardPrint("Added to pending " + message.getBody() + " from " + message.getSender() + " originally broadcast by " + message.getSource());

            History.addPendingMessage(message);
            History.checkPendingMessages(); /* TODO: Maybe move after rebroadcasting. */

            for (Integer neighbour : Configuration.SERVENT.neighbours()) {

                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
                Mailbox.sendMessage(message.makeMeASender()
                                           .changeReceiver(neighbour));

            }

        });

    }

//    public static Runnable BROADCAST(Message message) {
//
//        return new Thread(() -> {
//
//            if (message.getSource().ID() == Configuration.SERVENT.ID()) {
//                Logger.timestampedStandardPrint("Got own message back. No rebroadcast.");
//                return;
//            }
//
//            History.addPendingMessage(message);
//            History.checkPendingMessages();
//
//            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
//
//                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
//                Mailbox.sendMessage(message.makeMeASender()
//                        .changeReceiver(neighbour));
//
//            }
//
//        });
//
//    }
//
//    public static Runnable TRANSACTION(Message message) {
//
//        return new Thread(() -> {
//
//            if (message.getSource().ID() == Configuration.SERVENT.ID()) {
//                Logger.timestampedStandardPrint("Got own message back. No rebroadcast");
//                return;
//            }
//
//            History.addPendingMessage(message);
//            History.checkPendingMessages();
//
//            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
//
//                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
//                Mailbox.sendMessage(message.makeMeASender()
//                        .changeReceiver(neighbour));
//
//            }
//
//        });
//    }
//
//    public static Runnable ASK(Message message) {
//
//        return new Thread(() -> {
//
//            if (message.getSource().ID() == Configuration.SERVENT.ID()) {
//                Logger.timestampedStandardPrint("Got own message back. No rebroadcast");
//                return;
//            }
//
//            History.addPendingMessage(message);
//            History.checkPendingMessages();
//
//            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
//
//                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
//                Mailbox.sendMessage(message.makeMeASender()
//                        .changeReceiver(neighbour));
//
//            }
//
//        });
//
//    }
//
//    public static Runnable TELL(Message message) {
//
//        return new Thread(() -> {
//
//            if (message.getSource().ID() == Configuration.SERVENT.ID()) {
//                Logger.timestampedStandardPrint("Got own message back. No rebroadcast");
//                return;
//            }
//
//            History.addPendingMessage(message);
//            History.checkPendingMessages();
//
//            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
//
//                Logger.timestampedStandardPrint("Rebroadcasting: " + neighbour);
//                Mailbox.sendMessage(message.makeMeASender()
//                        .changeReceiver(neighbour));
//
//            }
//
//        });
//
//    }

}
