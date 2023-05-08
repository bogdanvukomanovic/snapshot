package servent.handler;

import app.Logger;
import servent.message.Message;
import servent.message.MessageType;

public class PongHandler implements MessageHandler {

    private Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.PONG) {
            /* Do nothing */
        }  else {
            Logger.timestampedErrorPrint("Handler for \"pong\" message got invalid message: " + clientMessage);
        }
    }

}
