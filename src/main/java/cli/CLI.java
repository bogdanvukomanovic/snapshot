package cli;

import app.Cancellable;
import app.Logger;
import cli.command.*;
import servent.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI implements Runnable, Cancellable {

    private volatile boolean working = true;

    private final List<Command> commands;

    public CLI(Listener listener) {

        this.commands = new ArrayList<>();

        commands.add(new InfoCommand());
        commands.add(new PauseCommand());
        commands.add(new BroadcastCommand());
        commands.add(new StopCommand(this, listener));

    }

    @Override
    public void run() {

        Scanner sc = new Scanner(System.in);

        while (working) {

            String line = sc.nextLine();

            int spacePos = line.indexOf(" ");

            String commandName;
            String commandArgs = null;
            if (spacePos != -1) {
                commandName = line.substring(0, spacePos);
                commandArgs = line.substring(spacePos + 1, line.length());
            } else {
                commandName = line;
            }

            boolean found = false;

            for (Command command : commands) {
                if (command.commandName().equals(commandName)) {
                    command.execute(commandArgs);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Logger.timestampedErrorPrint("Unknown command: " + commandName);
            }
        }

        sc.close();

    }

    @Override
    public void stop() {
        working = false;
    }

}
