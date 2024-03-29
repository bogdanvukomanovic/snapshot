package servent;

import app.Cancellable;
import app.Configuration;
import app.Logger;
import message.handler.MessageHandler;
import message.util.Mailbox;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Listener implements Runnable, Cancellable {

    private volatile boolean working = true;

    @Override
    public void run() {

        ServerSocket listenerSocket = null;

        try {

            listenerSocket = new ServerSocket(Configuration.SERVENT.port(), 100);

            // If there is no connection after 1 second, wake up and see if we should terminate.
            listenerSocket.setSoTimeout(1000);

        } catch (IOException e) {
            Logger.timestampedErrorPrint("Couldn't open listener socket on: " + Configuration.SERVENT.port());
            System.exit(0);
        }

        while (working) {

            try {

                // This blocks for up to 1 second, after which SocketTimeoutException is thrown.
                Socket clientSocket = listenerSocket.accept();
                MessageHandler.handleReceivedMessage(Mailbox.readMessage(clientSocket));

            } catch (SocketTimeoutException e) {
                // Uncomment the next line to see that we are waking up every second.
                // AppConfig.timedStandardPrint("Waiting...");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public void stop() {
        working = false;
    }

}
