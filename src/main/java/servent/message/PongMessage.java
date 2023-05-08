package servent.message;

import servent.Servent;

import java.io.Serial;

public class PongMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -7697721175488705446L;

    public PongMessage(Servent sender, Servent receiver) {
        super(MessageType.PONG, sender, receiver, "PONG");
    }

}
