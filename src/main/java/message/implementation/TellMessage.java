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

public class TellMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -7271037042461776570L;

    private Servent initiator;

    public TellMessage(Servent sender, Servent receiver, Servent initiator, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.TELL, sender, receiver, body, vectorClock);
        this.initiator = initiator;
    }

    public TellMessage(int ID, Servent source, MessageType type, Servent sender, Servent receiver, Servent initiator, List<Servent> route, String body, Map<Integer, Integer> vectorClock) {
        super(ID, source, type, sender, receiver, route, body, vectorClock);
        this.initiator = initiator;
    }

    public Servent getInitiator() {
        return initiator;
    }

    @Override
    public Message makeMeASender() {

        List<Servent> updatedRoute = new ArrayList<>(getRoute());
        updatedRoute.add(Configuration.SERVENT);

        return new TellMessage(getID(), getSource(), getMessageType(), Configuration.SERVENT, getReceiver(), initiator, updatedRoute, getBody(), getVectorClock());
    }

    @Override
    public Message changeReceiver(Integer newReceiverID) {

        Message result = null;

        if (Configuration.SERVENT.neighbours().contains(newReceiverID)) {

            result = new TellMessage(getID(), getSource(), getMessageType(), getSender(), Configuration.getServentByID(newReceiverID), initiator, getRoute(), getBody(), getVectorClock());

        } else {
            Logger.timestampedErrorPrint("Trying to make a message for " + newReceiverID + " who is not a neighbour.");
        }

        return result;
    }

}
