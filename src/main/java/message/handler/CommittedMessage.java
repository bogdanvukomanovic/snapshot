package message.handler;

import app.Configuration;
import message.Message;
import message.implementation.TellMessage;
import message.implementation.TransactionMessage;
import message.util.Mailbox;
import servent.History;
import servent.Servent;
import snapshot.Collector;
import snapshot.SnapshotState;
import snapshot.TransactionManager;

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

            Servent initiator = message.getSource();

            Message response = new TellMessage(Configuration.SERVENT, null, initiator, String.valueOf(TransactionManager.getCurrentBalance()), History.copyVectorClock());

            History.commitMessage(response);
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

}
