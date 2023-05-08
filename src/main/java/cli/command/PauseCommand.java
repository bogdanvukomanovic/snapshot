package cli.command;


import app.Logger;

public class PauseCommand implements Command {

	@Override
	public String commandName() {
		return "pause";
	}

	@Override
	public void execute(String args) {

		int timeToSleepMS = -1;
		
		try {
			timeToSleepMS = Integer.parseInt(args);
			
			if (timeToSleepMS < 0) {
				throw new NumberFormatException();
			}

			Logger.timestampedStandardPrint("Pausing for " + timeToSleepMS + " ms");
			try {
				Thread.sleep(timeToSleepMS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (NumberFormatException e) {
			Logger.timestampedErrorPrint("Command \"pause\" argument should be time given in ms.");
		}
	}

}
