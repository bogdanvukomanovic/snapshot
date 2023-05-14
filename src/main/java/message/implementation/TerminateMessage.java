package message.implementation;

import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.Map;

public class TerminateMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = 2538246482670986507L;

    public TerminateMessage(Servent sender, Servent receiver, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.TERMINATE, sender, receiver, body, vectorClock);
    }

}
