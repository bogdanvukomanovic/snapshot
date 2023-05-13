package app.start;

import app.Configuration;
import app.Logger;
import cli.CLI;
import servent.Listener;
import snapshot.Collector;
import snapshot.SnapshotState;
import snapshot.TransactionManager;

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

        SnapshotState.initializeGSS();
        SnapshotState.initializeLCS(Configuration.SERVENT.neighbours());

        Collector collector = new Collector(new TransactionManager());
        Thread collectorThread = new Thread(collector);
        collectorThread.start();

        Thread CLIThread = new Thread(new CLI(listener, collector));
        CLIThread.start();

    }

}