package cli.command;

import app.Configuration;
import app.Logger;
import servent.Servent;
import message.Message;
import message.PingMessage;
import message.util.MessageUtil;

public class PingCommand implements Command {

    @Override
    public String commandName() {
        return "ping";
    }

    @Override
    public void execute(String args) {

        int serventToPingID = -1;

        try {

            serventToPingID = Integer.parseInt(args);

            if (serventToPingID < 0 || serventToPingID >= Configuration.SERVENT_COUNT) {
                throw new NumberFormatException();
            }

            if (!Configuration.SERVENT.neighbours().contains(serventToPingID)) {
                Logger.timestampedErrorPrint("Trying to ping servent " + serventToPingID + " that is not our neighbour.");
                return;
            }

            Servent serventToPing = Configuration.getServentByID(serventToPingID);

            Message pingMessage = new PingMessage(Configuration.SERVENT, serventToPing);

            MessageUtil.sendMessage(pingMessage);

        } catch (NumberFormatException e) {
            Logger.timestampedErrorPrint("Command \"ping\" argument should be ID of servent that we want to ping.");
        }
    }

}
