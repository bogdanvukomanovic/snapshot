package cli.command;

import app.Configuration;
import app.Logger;
import message.Message;
import message.BroadcastMessage;
import message.util.MessageUtil;
import servent.History;

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

        Message message = new BroadcastMessage(Configuration.SERVENT, null, args, History.copyVectorClock());

        History.commitMessage(message);
        History.checkPendingMessages();

        for (Integer neighbor : Configuration.SERVENT.neighbours()) {
            MessageUtil.sendMessage(message.changeReceiver(neighbor));
        }

    }

}
