package message.util;

import app.Logger;
import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Mailbox {

    public static final boolean MAILBOX_PRINTING = true;

    public static Message readMessage(Socket socket) {

        Message clientMessage = null;

        try {

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            clientMessage = (Message) ois.readObject();

            socket.close();

        } catch (IOException e) {
            Logger.timestampedErrorPrint("Error in reading socket on " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (MAILBOX_PRINTING) {
            Logger.timestampedStandardPrint("Got message " + clientMessage);
        }

        return clientMessage;
    }

    public static void sendMessage(Message message) {

        Thread delayedSender = new Thread(new DelayedMessageSender(message));

        delayedSender.start();
    }

}
