package cli.command;

public interface Command {

    String commandName();

    void execute(String args);

}
