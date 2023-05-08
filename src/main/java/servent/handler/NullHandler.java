package servent.handler;

import app.Logger;
import servent.message.Message;

public class NullHandler implements MessageHandler {

    private final Message clientMessage;

    public NullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        Logger.timestampedErrorPrint("Couldn't handle message: " + clientMessage);
    }

}
