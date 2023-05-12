package message.implementation;

import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.Map;

public class AskMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = 8494652350613378241L;

    public AskMessage(Servent sender, Servent receiver, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.ASK, sender, receiver, body, vectorClock);
    }

}
