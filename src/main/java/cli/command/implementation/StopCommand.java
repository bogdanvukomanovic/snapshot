package cli.command.implementation;


import app.Logger;
import cli.CLI;

import cli.command.Command;
import servent.Listener;


public class StopCommand implements Command {

	private CLI cli;
	private Listener listener;
	
	public StopCommand(CLI cli, Listener listener) {
		this.cli = cli;
		this.listener = listener;
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
	}

}
