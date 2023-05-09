package message;

import servent.Servent;

import java.util.Map;

public class BroadcastMessage extends BasicMessage {
    public BroadcastMessage(Servent sender, Servent receiver, String body, Map<Integer, Integer> vectorClock) {
        super(MessageType.BROADCAST, sender, receiver, body, vectorClock);
    }

}
