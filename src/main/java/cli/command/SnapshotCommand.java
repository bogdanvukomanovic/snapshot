package cli.command;

import snapshot.Collector;

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
        collector.startCollecting();
    }

}
