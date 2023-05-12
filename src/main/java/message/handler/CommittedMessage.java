package message.handler;

import app.Configuration;
import message.Message;
import message.implementation.TransactionMessage;
import snapshot.Collector;
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

}
