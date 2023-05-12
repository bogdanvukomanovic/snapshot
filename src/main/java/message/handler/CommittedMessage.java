package message.handler;

import app.Configuration;
import app.Logger;
import message.Message;
import message.implementation.TellMessage;
import message.implementation.TransactionMessage;
import message.util.Mailbox;
import servent.History;
import servent.Servent;
import snapshot.Collector;
import snapshot.State;
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

            /* Works */
            // Logger.timestampedErrorPrint(String.valueOf(((TellMessage) message).getInitiator().ID()));
            // Logger.timestampedErrorPrint(message.getBody());

            /* Doesn't work */
            // Logger.timestampedErrorPrint(message.getBody());

            /* Interesting */
            // try {
            //    Logger.timestampedErrorPrint(String.valueOf(((TellMessage) message).getInitiator().ID()));
            // } catch (Exception e) {
            //    Logger.timestampedErrorPrint(e.getMessage());
            // }

            /* I'm an idiot */
            // class message.implementation.BasicMessage cannot be cast to class message.implementation.TellMessage (message.implementation.BasicMessage and message.implementation.TellMessage are in unnamed module of loader 'app')

            /* Bad */
            State.GSS.put(message.getSource().ID(), Integer.parseInt(message.getBody()));

            // if (Configuration.SERVENT.ID() == ((TellMessage) message).getInitiator().ID()) {
            //    State.GSS.put(message.getSource().ID(), Integer.parseInt(message.getBody()));
            //    return;
            // }

            /* TODO: Remove this. */
            // for (Integer neighbour : Configuration.SERVENT.neighbours()) {
            //    Mailbox.sendMessage(message.changeReceiver(neighbour));
            // }

        });

    }

}
