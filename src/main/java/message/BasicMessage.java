package message;

import app.Configuration;
import app.Logger;
import servent.Servent;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicMessage implements Message {

    /* TODO: Not handling Vector Clock logic at the moment. */

    @Serial
    private static final long serialVersionUID = -8860410919735866577L;

    private final int ID;
    private final MessageType type;
    private final Servent source;
    private final Servent sender;
    private final Servent receiver;
    private final List<Servent> route;
    private final String body;
    private Map<Integer, Integer> vectorClock;

    private static AtomicInteger messageCounter = new AtomicInteger(0);

    public BasicMessage(MessageType type, Servent sender, Servent receiver, String body, Map<Integer, Integer> vectorClock) {

        this.ID = messageCounter.getAndIncrement();
        this.source = sender;

        this.type = type;
        this.sender = sender;
        this.receiver = receiver;

        this.route = new ArrayList<>();
        this.route.add(sender);

        this.body = body;
        this.vectorClock = vectorClock;

    }

    private BasicMessage(int ID, Servent source, MessageType type, Servent sender, Servent receiver, List<Servent> route, String body, Map<Integer, Integer> vectorClock) {

        this.ID = ID;
        this.source = source;

        this.type = type;
        this.sender = sender;
        this.receiver = receiver;

        this.route = route;

        this.body = body;
        this.vectorClock = vectorClock;

    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public MessageType getMessageType() {
        return type;
    }

    @Override
    public Servent getSource() {
        return source;
    }

    @Override
    public Servent getSender() {
        return sender;
    }

    @Override
    public Servent getReceiver() {
        return receiver;
    }

    @Override
    public List<Servent> getRoute() {
        return route;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public Map<Integer, Integer> getVectorClock() {
        return vectorClock;
    }

    @Override
    public Message makeMeASender() {

        List<Servent> updatedRoute = new ArrayList<>(route);
        updatedRoute.add(Configuration.SERVENT);

        return new BasicMessage(getID(), getSource(), getMessageType(), Configuration.SERVENT, getReceiver(), updatedRoute, getBody(), getVectorClock());
    }

    @Override
    public Message changeReceiver(Integer newReceiverID) {

        Message result = null;

        if (Configuration.SERVENT.neighbours().contains(newReceiverID)) {

            result = new BasicMessage(getID(), getSource(), getMessageType(), getSender(), Configuration.getServentByID(newReceiverID), getRoute(), getBody(), getVectorClock());

        } else {
            Logger.timestampedErrorPrint("Trying to make a message for " + newReceiverID + " who is not a neighbour.");
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof BasicMessage) {

            BasicMessage other = (BasicMessage) obj;

            if (getID() == other.getID() && getSource().ID() == other.getSource().ID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID(), getSource().ID());
    }

    @Override
    public String toString() {

        /* TODO: Maybe add source. */

        return "[" + getSender().ID() + "|" + getID() + "|" +
                getBody() + "|" + getMessageType() + "|" +
                getReceiver().ID() + "]";
    }

}
