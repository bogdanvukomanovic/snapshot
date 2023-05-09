package message.util;

import app.Logger;
import servent.Servent;
import message.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DelayedMessageSender implements Runnable {

    private Message message;

    private static final int MIN_DELAY_MS = 500;
    private static final int DELAY_INTERVAL_MS = 1000;

    public DelayedMessageSender(Message message) {
        this.message = message;
    }

    public void run() {

        // A random sleep before sending in order to simulate delayed message sending.
        try {
            Thread.sleep((long)(Math.random() * DELAY_INTERVAL_MS) + MIN_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Servent receiver = message.getReceiver();

        if (MessageUtil.MESSAGE_UTIL_PRINTING) {
            Logger.timestampedStandardPrint("Sending message " + message);
        }

        try {

            Socket sendSocket = new Socket(receiver.address(), receiver.port());

            ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject(message);
            oos.flush();

            sendSocket.close();

        } catch (IOException e) {
            Logger.timestampedErrorPrint("Couldn't send message: " + message.toString());
        }

    }

}
