package snapshot;

import app.Cancellable;
import app.Configuration;
import app.Logger;
import message.Message;
import message.implementation.AskMessage;
import message.util.Mailbox;
import servent.History;

import java.util.concurrent.atomic.AtomicBoolean;

public class Collector implements Runnable, Cancellable {

    private volatile boolean working = true;
    private AtomicBoolean collecting = new AtomicBoolean(false);

    public static TransactionManager transactionManager;

    public Collector(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void run() {

        while (working) {

            while (!collecting.get()) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!working) {
                    return;
                }

            }

            /* TODO: Do collecting stuff. */

            /* [1] BROADCAST ASK MESSAGE */
            Message message = new AskMessage(Configuration.SERVENT, null, "ASK", History.copyVectorClock());

            History.commitMessage(message);
            History.checkPendingMessages();

            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
                Mailbox.sendMessage(message.changeReceiver(neighbour));
            }

            /* [2] WAIT FOR RESPONSES (TELL MESSAGES) */
            while (State.getGlobalServentState().size() != Configuration.SERVENT_COUNT) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!working) {
                    return;
                }

            }

            Logger.newLineBarrierPrint("GSS: " + State.getGlobalServentState());

            /* [3] CALCULATE RESULTS */
            /* [4] PRINT RESULTS */

            collecting.set(false); /* TODO: Check this. */
        }

    }

    public void startCollecting() {

        if (this.collecting.getAndSet(true)) {
            Logger.timestampedErrorPrint("Tried to start collecting before finished with previous.");
        }

    }

    @Override
    public void stop() {
        working = false;
    }

    public static TransactionManager getTransactionManager() {
        return transactionManager;
    }

}
