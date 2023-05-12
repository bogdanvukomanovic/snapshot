package cli.command.implementation;

import app.Configuration;
import app.Logger;
import cli.command.Command;
import message.Message;
import message.implementation.BroadcastMessage;
import message.util.Mailbox;
import servent.History;

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

        for (Integer neighbour : Configuration.SERVENT.neighbours()) {
            Mailbox.sendMessage(message.changeReceiver(neighbour));
        }

    }

}
