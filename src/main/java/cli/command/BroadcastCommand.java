package cli.command;

import app.Configuration;
import app.Logger;
import message.Message;
import message.BroadcastMessage;
import message.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class BroadcastCommand implements Command {

    @Override
    public String commandName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {


        if (args == null) {
            Logger.timestampedErrorPrint("No message to broadcast.");
            return;
        }

        String body = args;
        Map<Integer, Integer> vectorClock = new HashMap<>();

        Message message = new BroadcastMessage(Configuration.SERVENT, null, body, vectorClock);

        for (Integer neighbor : Configuration.SERVENT.neighbours()) {

            message = message.changeReceiver(neighbor);

            MessageUtil.sendMessage(message);
        }
    }

}
