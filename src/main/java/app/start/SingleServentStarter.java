package app.start;

import app.Configuration;
import app.Logger;
import cli.CLI;
import servent.Listener;

public class SingleServentStarter {

    /**
     * Command line arguments are:
     *      > args[0] - Path to .properties file
     *      > args[1] - This Servent's ID
     */
    public static void main(String[] args) {

        /* TODO: Assert that command line arguments are valid. */

        Configuration.load(args[0]);
        Configuration.SERVENT = Configuration.getServentByID(Integer.parseInt(args[1]));

        Logger.timestampedStandardPrint("Starting servent " + Configuration.SERVENT);

        Listener listener = new Listener();
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        Thread CLIThread = new Thread(new CLI(listener));
        CLIThread.start();

    }

}