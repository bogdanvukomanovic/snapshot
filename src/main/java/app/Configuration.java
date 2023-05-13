package app;

import servent.History;
import servent.Servent;
import snapshot.SnapshotType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Configuration {

    public static int SERVENT_COUNT;
    public static Servent SERVENT;
    public  static List<Servent> SERVENTS = new ArrayList<>();

    public static SnapshotType SNAPSHOT;

    private static List<Integer> collectNeighbours(Properties properties, int ID) {
        return Stream.of(properties.getProperty("SERVENT_" + ID + ".NEIGHBOURS").split(",")).map(x -> Integer.parseInt(x)).toList();
    }

    public static void load(String configuration) {

        /* TODO: Assert that .properties file syntax is valid. */
        /* TODO: Assert that port numbers are in correct range. */

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(configuration));
        } catch (IOException e) {
            Logger.timestampedErrorPrint("Couldn't open properties file. Exiting...");
            System.exit(0);
        }

        SERVENT_COUNT = Integer.parseInt(properties.getProperty("SERVENT_COUNT"));

        for (int i = 0; i < SERVENT_COUNT; i++) {

            String address = properties.getProperty("SERVENT_" + i + ".ADDRESS");
            int port = Integer.parseInt(properties.getProperty("SERVENT_" + i + ".PORT"));

            SERVENTS.add(i, new Servent(i, address, port, collectNeighbours(properties, i)));
        }

        History.initializeVectorClock(SERVENT_COUNT);

        switch (properties.getProperty("SNAPSHOT", "NONE")) {

            case "ACHARYA_BADRINATH":
                SNAPSHOT = SnapshotType.ACHARYA_BADRINATH;
                break;
            case "ALAGAR_VENKATESAN":
                SNAPSHOT = SnapshotType.ALAGAR_VENKATESAN;
            case "NONE":
                SNAPSHOT = SnapshotType.NONE;
                break;

        }

    }

    public static Servent getServentByID(int ID) {

        if (ID >= SERVENT_COUNT) {
            throw new IllegalArgumentException("Trying to get info for servent " + ID + " when there are " + SERVENT_COUNT + " servents.");
        }

        return SERVENTS.get(ID);
    }

}