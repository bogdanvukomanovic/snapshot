package message.implementation;

import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.Map;

public class TransactionMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -560153192124028324L;

    private Servent payee;

    public TransactionMessage(Servent sender, Servent receiver, Servent payee, int amount, Map<Integer, Integer> vectorClock) {
        super(MessageType.TRANSACTION, sender, receiver, String.valueOf(amount), vectorClock);
        this.payee = payee;
    }

    public Servent getPayee() {
        return payee;
    }

}
