package snapshot;

import app.Cancellable;
import app.Configuration;
import app.Logger;
import message.Message;
import message.implementation.AskMessage;
import message.implementation.TerminateMessage;
import message.util.Mailbox;
import servent.History;

import java.util.Map;
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

            Message message;

            /* [1] BROADCAST ASK MESSAGE */
            message = new AskMessage(Configuration.SERVENT, null, "ASK", History.copyVectorClock());

            History.addPendingMessage(message);
            History.checkPendingMessages();

            for (Integer neighbour : Configuration.SERVENT.neighbours()) {
                Mailbox.sendMessage(message.changeReceiver(neighbour));
            }

            /* [2] WAIT FOR RESPONSES (TELL MESSAGES) */
            while (SnapshotState.getGSS().size() != Configuration.SERVENT_COUNT) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!working) {
                    return;
                }

            }

            if (Configuration.SNAPSHOT == SnapshotType.ALAGAR_VENKATESAN) {

                message = new TerminateMessage(Configuration.SERVENT, null, "TERMINATE", History.copyVectorClock());

                History.addPendingMessage(message);
                History.checkPendingMessages();

                for (Integer neighbour : Configuration.SERVENT.neighbours()) {
                    Mailbox.sendMessage(message.changeReceiver(neighbour));
                }

            }

            /* [3] CALCULATE AND PRINT RESULTS */
            Logger.newLineBarrierPrint("SNAPSHOT SUMMARY:");
            int total = 0;
            for (Map.Entry<Integer, Snap> entry : SnapshotState.GSS.entrySet()) {

                Logger.timestampedStandardPrint("Servent " + entry.getKey() + " have " + entry.getValue().currentBalance() + " tokens.");
                total += entry.getValue().currentBalance();

            }

            Logger.newLineBarrierPrint("Together they have " + total + " tokens.");

            if (Configuration.SNAPSHOT == SnapshotType.ACHARYA_BADRINATH) {
                calculateChannelState();
            }


            endCollecting();
            resetCollectingMetadata();
        }

    }

    private int calculateChannelState() {

        int total = 0;

        for (int i = 0; i < Configuration.SERVENT_COUNT; i++) {

            for (int j = 0; j < Configuration.SERVENT_COUNT; j++) {

                if (i == j) { continue; }

                if (!Configuration.getServentByID(i).neighbours().contains(Configuration.getServentByID(j).ID())) {
                    continue;
                }

                Integer iServentID = Configuration.getServentByID(i).ID();
                Integer jServentID = Configuration.getServentByID(j).ID();

                int x = SnapshotState.GSS.get(iServentID).sent().get(jServentID) - SnapshotState.GSS.get(jServentID).received().get(iServentID);

                if (x > 0) {
                    Logger.timestampedStandardPrint("Servent " + jServentID + " did not receive " + x + " tokens from Servent " + iServentID);
                    total += x;
                }

            }
        }

        Logger.newLineBarrierPrint("There are " + total + " tokens in channels.");

        return total;
    }

    public void startCollecting() {

        if (this.collecting.getAndSet(true)) {
            Logger.timestampedErrorPrint("Tried to start collecting before finished with previous.");
        }

    }

    private void endCollecting() {
        collecting.set(false);
    }

    private void resetCollectingMetadata() {
        SnapshotState.resetGlobalState();
    }

    @Override
    public void stop() {
        working = false;
    }

    public static TransactionManager getTransactionManager() {
        return transactionManager;
    }

}
