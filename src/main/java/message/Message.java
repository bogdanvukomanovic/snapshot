package message;

import servent.Servent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Message extends Serializable {

    int getID();
    MessageType getMessageType();
    Servent getSource();
    Servent getSender();
    Servent getReceiver();
    List<Servent> getRoute();
    String getBody();
    Map<Integer, Integer> getVectorClock();
    Message makeMeASender();
    Message changeReceiver(Integer newReceiverID);

}
