package servent.handler;

import app.Logger;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

public class PingHandler implements MessageHandler {

    private final Message clientMessage;

    public PingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.PING) {
            MessageUtil.sendMessage(
                    new PongMessage(clientMessage.getReceiver(), clientMessage.getOriginalSender())
            );
        } else {
            Logger.timestampedErrorPrint("Handler for \"ping\" message got invalid message: " + clientMessage);
        }
    }

}
