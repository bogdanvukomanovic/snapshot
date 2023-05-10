package cli.command;

import app.Logger;
import message.Message;
import servent.History;

public class PrintCausalCommand implements Command {

    @Override
    public String commandName() {
        return "print_causal";
    }

    @Override
    public void execute(String args) {

        int i = 0;

        Logger.timestampedStandardPrint("Current causal messages:");

        for (Message message: History.getCommittedMessages()) {
            Logger.timestampedStandardPrint("\t> Message " + i++ + ": " + message.getBody() + " from " + message.getSource().ID());
        }

    }

}
