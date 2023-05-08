package cli.command;

import app.Configuration;
import app.Logger;

public class InfoCommand implements Command {

    @Override
    public String commandName() {
        return "info";
    }

    @Override
    public void execute(String args) {

        Logger.timestampedStandardPrint("My info: " + Configuration.SERVENT);
        Logger.timestampedStandardPrint("Neighbours: ");

        String neighbours = "";

        for (Integer neighbour : Configuration.SERVENT.neighbours()) {
            neighbours += neighbour + " ";
        }

        Logger.timestampedStandardPrint(neighbours);

    }

}
