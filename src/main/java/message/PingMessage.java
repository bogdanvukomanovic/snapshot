package message;

import servent.Servent;

import java.io.Serial;

public class PingMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = -8023768317829253229L;

    public PingMessage(Servent sender, Servent receiver) {
        super(MessageType.PING, sender, receiver, "PING");
    }

}
