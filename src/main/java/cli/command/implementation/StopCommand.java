package cli.command.implementation;


import app.Logger;
import cli.CLI;

import cli.command.Command;
import servent.Listener;
import snapshot.Collector;


public class StopCommand implements Command {

	private CLI cli;
	private Listener listener;
	private Collector collector;
	
	public StopCommand(CLI cli, Listener listener, Collector collector) {
		this.cli = cli;
		this.listener = listener;
		this.collector = collector;
	}
	
	@Override
	public String commandName() {
		return "stop";
	}

	@Override
	public void execute(String args) {
		Logger.timestampedStandardPrint("Stopping...");
		cli.stop();
		listener.stop();
		collector.stop();
	}

}
