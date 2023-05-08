package servent.message;

import app.Configuration;
import app.Logger;
import servent.Servent;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicMessage implements Message {

    @Serial
    private static final long serialVersionUID = -8860410919735866577L;

    private final MessageType type;
    private final Servent originalSender;
    private final Servent receiver;
    private final List<Servent> route;
    private final String messageText;
    private static AtomicInteger messageCounter = new AtomicInteger(0);
    private final int messageID;

    public BasicMessage(MessageType type, Servent originalSender, Servent receiver) {
        this.type = type;
        this.originalSender = originalSender;
        this.receiver = receiver;
        this.route = new ArrayList<>();
        this.messageText = "";
        this.messageID = messageCounter.getAndIncrement();
    }

    public BasicMessage(MessageType type, Servent originalSender, Servent receiver, String messageText) {
        this.type = type;
        this.originalSender = originalSender;
        this.receiver = receiver;
        this.route = new ArrayList<>();
        this.messageText = messageText;
        this.messageID = messageCounter.getAndIncrement();
    }

    @Override
    public Servent getOriginalSender() {
        return originalSender;
    }

    @Override
    public List<Servent> getRoute() {
        return route;
    }

    @Override
    public Servent getReceiver() {
        return receiver;
    }

    @Override
    public MessageType getMessageType() {
        return type;
    }

    @Override
    public String getMessageText() {
        return messageText;
    }

    @Override
    public int getMessageID() {
        return messageID;
    }

    private BasicMessage(MessageType type, Servent originalSender, Servent receiver, List<Servent> route, String messageText, int messageID) {
        this.type = type;
        this.originalSender = originalSender;
        this.receiver = receiver;
        this.route = route;
        this.messageText = messageText;
        this.messageID = messageID;
    }

    @Override
    public Message makeMeASender() {

        Servent newRouteItem = Configuration.SERVENT;

        List<Servent> newRouteList = new ArrayList<>(route);
        newRouteList.add(newRouteItem);

        Message result = new BasicMessage(getMessageType(), getOriginalSender(), getReceiver(), newRouteList, getMessageText(), getMessageID());

        return result;
    }

    @Override
    public Message changeReceiver(Integer newReceiverID) {

        Message result = null;

        if (Configuration.SERVENT.neighbours().contains(newReceiverID)) {
            Servent newReceiver = Configuration.getServentByID(newReceiverID);

            result = new BasicMessage(getMessageType(), getOriginalSender(), newReceiver, getRoute(), getMessageText(), getMessageID());

        } else {
            Logger.timestampedErrorPrint("Trying to make a message for " + newReceiverID + " who is not a neighbour.");
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof BasicMessage) {
            BasicMessage other = (BasicMessage) obj;

            if (getMessageID() == other.getMessageID() &&
                    getOriginalSender().ID() == other.getOriginalSender().ID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageID(), getOriginalSender().ID());
    }

    @Override
    public String toString() {
        return "[" + getOriginalSender().ID() + "|" + getMessageID() + "|" +
                getMessageText() + "|" + getMessageType() + "|" +
                getReceiver().ID() + "]";
    }

}
