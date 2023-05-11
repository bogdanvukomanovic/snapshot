package cli.command.implementation;

import app.Configuration;
import app.Logger;
import cli.command.Command;
import message.Message;
import servent.History;

public class InfoCommand implements Command {

    @Override
    public String commandName() {
        return "info";
    }

    @Override
    public void execute(String args) {

        Logger.emptyLine();
        Logger.timestampedStandardPrint("My info: " + Configuration.SERVENT);

        String neighbours = "";

        for (Integer neighbour : Configuration.SERVENT.neighbours()) {
            neighbours += neighbour + " ";
        }

        Logger.timestampedStandardPrint("My neighbours ID's: " + neighbours);


        int i = 0;

        Logger.emptyLine();
        Logger.timestampedStandardPrint("Currently committed messages:");

        for (Message message: History.getCommittedMessages()) {
            Logger.timestampedStandardPrint("Message " + i++ + ": " + message.getBody() + " from " + message.getSource().ID());
        }

        Logger.newLineBarrierPrint("Vector clock: " + History.getVectorClock());

    }

}
