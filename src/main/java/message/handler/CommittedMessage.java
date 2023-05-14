package message.handler;

import app.Configuration;
import app.Logger;
import message.Message;
import message.MessageType;
import message.implementation.TellMessage;
import message.implementation.TransactionMessage;
import message.util.Mailbox;
import servent.History;
import servent.Servent;
import snapshot.Collector;
import snapshot.SnapshotState;
import snapshot.SnapshotType;
import snapshot.TransactionManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommittedMessage {

    public static Runnable TRANSACTION(Message message) {

        return new Thread(() -> {

            TransactionManager manager = Collector.getTransactionManager();

            if (Configuration.SERVENT.ID() == message.getSource().ID()) {
                manager.subtract(Integer.parseInt(message.getBody()));
                return;
            }

            if (Configuration.SERVENT.ID() == ((TransactionMessage) message).getPayee().ID()) {
                manager.add(Integer.parseInt(message.getBody()));
            }

        });

    }

    public static Runnable ASK(Message message) {

        return new Thread(() -> {

            if (Configuration.SNAPSHOT == SnapshotType.ALAGAR_VENKATESAN) {
                SnapshotState.initializeLCS(Configuration.SERVENT.neighbours());
                SnapshotState.token = (Optional.of(message));
            }

            Servent initiator = message.getSource();

            Message response = new TellMessage(Configuration.SERVENT, null, initiator, String.valueOf(TransactionManager.getCurrentBalance()), History.copyVectorClock());

            History.addPendingMessage(response);
            History.checkPendingMessages();

            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
                Mailbox.sendMessage(response.changeReceiver(neighbour));
            }

        });

    }

    public static Runnable TELL(Message message) {

        return new Thread(() -> {

            if (Configuration.SERVENT.ID() == ((TellMessage) message).getInitiator().ID()) {
                SnapshotState.GSS.put(message.getSource().ID(), Integer.parseInt(message.getBody()));
            }

        });

    }

    public static Runnable TERMINATE(Message message) {

        /* TODO: Change logs in this function, it is convenient to print them to stderr at the moment. */

        return new Thread(() -> {

            for (Map.Entry<Integer, List<Message>> entry : SnapshotState.LCS.entrySet()) {

                int amount = 0;

                for (Message channelMessage : entry.getValue()) {

                    if (channelMessage.getMessageType() == MessageType.TRANSACTION) {
                        // Logger.timestampedErrorPrint("PAYER: " + ((TransactionMessage) channelMessage).getSource().ID() + " PAYEE: " + ((TransactionMessage) channelMessage).getPayee().ID());
                        if (Configuration.SERVENT.ID() == ((TransactionMessage) channelMessage).getPayee().ID()) {
                            amount += Integer.parseInt(channelMessage.getBody());
                        }

                    }

                }

                Logger.timestampedErrorPrint("Servent " + Configuration.SERVENT.ID() + " did not receive " + amount + " tokens from Servent " + entry.getKey());

            }

            Logger.timestampedErrorPrint("- - - - - - - - - - - - - - -");

        });

    }

}
