package cli.command;


import app.Logger;
import cli.CLI;

import servent.ServentListener;


public class StopCommand implements Command {

	private CLI cli;
	private ServentListener listener;
	
	public StopCommand(CLI cli, ServentListener listener) {
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
