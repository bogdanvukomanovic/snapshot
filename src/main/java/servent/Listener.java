package servent;

import app.Cancellable;
import app.Configuration;
import app.Logger;
import message.Message;
import message.handler.MessageHandler;
import message.handler.NullHandler;
import message.handler.PingHandler;
import message.handler.PongHandler;
import message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener implements Runnable, Cancellable {

    private volatile boolean working = true;
    private final ExecutorService service = Executors.newWorkStealingPool();

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

                Message clientMessage = MessageUtil.readMessage(clientSocket);
                MessageHandler messageHandler = new NullHandler(clientMessage);

                switch (clientMessage.getMessageType()) {
                    case PING:
                        messageHandler = new PingHandler(clientMessage);
                        break;
                    case PONG:
                        messageHandler = new PongHandler(clientMessage);
                        break;
                }

                service.submit(messageHandler);

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
