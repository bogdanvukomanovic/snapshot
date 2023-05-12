package snapshot;

import app.Cancellable;
import app.Logger;

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
            /* [2] WAIT FOR RESPONSES (TELL MESSAGES) */
            /* [3] CALCULATE RESULTS */
            /* [4] PRINT RESULTS */

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
