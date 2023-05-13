package cli.command.implementation;

import app.Configuration;
import app.Logger;
import cli.command.Command;
import snapshot.Collector;
import snapshot.SnapshotType;

public class SnapshotCommand implements Command {

    private Collector collector;

    public SnapshotCommand(Collector collector) {
        this.collector = collector;
    }

    @Override
    public String commandName() {
        return "snapshot";
    }

    @Override
    public void execute(String args) {

        if (Configuration.SNAPSHOT == SnapshotType.NONE) {
            Logger.timestampedErrorPrint("Snapshot denied. Please specify snapshot type in .properties file.");
        }

        collector.startCollecting();
    }

}
