package cli.command.implementation;

import app.Configuration;
import cli.command.Command;
import message.Message;
import message.implementation.TransactionMessage;
import message.util.Mailbox;
import servent.History;
import servent.Servent;

import java.util.concurrent.ThreadLocalRandom;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 5;
    private static final int BURST_WORKERS = 10;
    private static final int MAX_TRANSFER_AMOUNT = 10;

    private class TransactionBurstWorker implements Runnable {

        @Override
        public void run() {

            ThreadLocalRandom random = ThreadLocalRandom.current();

            for (int i = 0; i < TRANSACTION_COUNT; i++) {

                // It could be fun to choose payee randomly.

                for (int neighbour : Configuration.SERVENT.neighbours()) {

                    Servent receiver = Configuration.getServentByID(neighbour);

                    int amount = 1 + random.nextInt(MAX_TRANSFER_AMOUNT);

                    /**
                     * Since in this transaction relation we are the payer, this is the place where we can specify
                     * the payee. For demonstration purposes we exchange resources with our neighbours.
                     **/
                    Servent payee = receiver;

                    Message message = new TransactionMessage(Configuration.SERVENT, receiver, payee, amount, History.copyVectorClock());

                    History.commitMessage(message);
                    History.checkPendingMessages();

                    Mailbox.sendMessage(message);

                    /* Since we committed this message we can subtract given amount from our balance. */
                    /* When our neighbour (payee) commit this message, he can add given amount to its balance. */
                }

            }
        }
    }

    @Override
    public String commandName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {

        for (int i = 0; i < BURST_WORKERS; i++) {

            Thread t = new Thread(new TransactionBurstWorker());
            t.start();

        }

    }

}
