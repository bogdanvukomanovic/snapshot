package message.implementation;

import app.Configuration;
import app.Logger;
import message.Message;
import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -560153192124028324L;

    private Servent payee;

    public TransactionMessage(Servent sender, Servent receiver, Servent payee, int amount, Map<Integer, Integer> vectorClock) {
        super(MessageType.TRANSACTION, sender, receiver, String.valueOf(amount), vectorClock);
        this.payee = payee;
    }

    public TransactionMessage(int ID, Servent source, MessageType type, Servent sender, Servent receiver, Servent payee, List<Servent> route, String body, Map<Integer, Integer> vectorClock) {
        super(ID, source, type, sender, receiver, route, body, vectorClock);
        this.payee = payee;
    }


    public Servent getPayee() {
        return payee;
    }

    @Override
    public Message makeMeASender() {

        List<Servent> updatedRoute = new ArrayList<>(getRoute());
        updatedRoute.add(Configuration.SERVENT);

        return new TransactionMessage(getID(), getSource(), getMessageType(), Configuration.SERVENT, getReceiver(), payee, updatedRoute, getBody(), getVectorClock());
    }

    @Override
    public Message changeReceiver(Integer newReceiverID) {

        Message result = null;

        if (Configuration.SERVENT.neighbours().contains(newReceiverID)) {

            result = new TransactionMessage(getID(), getSource(), getMessageType(), getSender(), Configuration.getServentByID(newReceiverID), payee, getRoute(), getBody(), getVectorClock());

        } else {
            Logger.timestampedErrorPrint("Trying to make a message for " + newReceiverID + " who is not a neighbour.");
        }

        return result;
    }

    public void setPayee(Servent payee) {
        this.payee = payee;
    }
}
