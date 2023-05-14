package cli.command.implementation;

import app.Configuration;
import cli.command.Command;
import message.implementation.TransactionMessage;
import message.util.Mailbox;
import servent.History;
import servent.Servent;

import java.util.concurrent.ThreadLocalRandom;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 15;
    private static final int MAX_TRANSFER_AMOUNT = 10;

    @Override
    public String commandName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < TRANSACTION_COUNT; i++) {

            /**
             * Since in this transaction relation we are the payer, this is the place where we can specify
             * the payee. For demonstration purposes we exchange resources with our neighbours. It could be
             * fun to choose payee randomly.
             **/

            for (int payee : Configuration.SERVENT.neighbours()) {

                int amount = 1 + random.nextInt(MAX_TRANSFER_AMOUNT);

                TransactionMessage message = new TransactionMessage(Configuration.SERVENT, null, null, amount, History.copyVectorClock());

                History.addPendingMessage(message);
                History.checkPendingMessages();

                for (int neighbour : Configuration.SERVENT.neighbours()) {

                    Servent receiver = Configuration.getServentByID(neighbour);
                    message.setPayee(Configuration.getServentByID(payee));

                    Mailbox.sendMessage(message.changeReceiver(receiver.ID()));

                }

            }

        }

    }

}