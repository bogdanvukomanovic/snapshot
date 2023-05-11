package message.implementation;

import message.MessageType;
import servent.Servent;

import java.io.Serial;
import java.util.Map;

public class BroadcastMessage extends BasicMessage {
    @Serial
    private static final long serialVersionUID = 3130070560114038882L;

    public BroadcastMessage(Servent sender, Servent receiver, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.BROADCAST, sender, receiver, body, vectorClock);
    }

}
