package cli.command;

import app.Configuration;
import app.Logger;
import message.handler.Handler;
import message.implementation.BroadcastMessage;
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

        Handler.handleRequestedMessage(new BroadcastMessage(Configuration.SERVENT, null, args, History.copyVectorClock()));

    }

}
