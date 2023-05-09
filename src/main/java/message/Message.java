package message;

import servent.Servent;

import java.io.Serializable;
import java.util.List;

public interface Message extends Serializable {

    Servent getOriginalSender();
    List<Servent> getRoute();
    Servent getReceiver();
    MessageType getMessageType();
    String getMessageText();
    int getMessageID();
    Message makeMeASender();
    Message changeReceiver(Integer newReceiverID);

}
