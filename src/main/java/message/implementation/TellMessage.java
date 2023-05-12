package message.implementation;

import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.Map;

public class TellMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -7271037042461776570L;

    private Servent initiator;

    public TellMessage(Servent sender, Servent receiver, Servent initiator, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.TELL, sender, receiver, body, vectorClock);
        this.initiator = initiator;
    }

    public Servent getInitiator() {
        return initiator;
    }

}
