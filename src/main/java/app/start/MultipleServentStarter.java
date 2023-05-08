package app.start;

import app.Configuration;
import app.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultipleServentStarter {

    public static void main(String[] args) {

        String test = "ping";

        start(test);

    }

    private static class ServentCLI implements Runnable {

        private List<Process> processes;

        public ServentCLI(List<Process> processes) {
            this.processes = processes;
        }

        @Override
        public void run() {

            Scanner sc = new Scanner(System.in);

            while (true) {

                String line = sc.nextLine();

                if (line.equals("stop")) {
                    for (Process process : processes) {
                        process.destroy();
                    }
                    break;
                }
            }

            sc.close();
        }
    }

    private static void start(String test) {

        List<Process> processes = new ArrayList<>();

        Configuration.load("./src/main/resources/" + test + "/servent_list.properties");

        Logger.timestampedStandardPrint("Starting multiple servent runner.");
        Logger.timestampedStandardPrint("If servents do not finish on their own type \"stop\" to finish them.");

        for (int i = 0; i < Configuration.SERVENT_COUNT; i++) {

            try {

                ProcessBuilder builder = new ProcessBuilder("java", "-cp", "target/classes", "app.start.SingleServentStarter", "./src/main/resources/" + test + "/servent_list.properties", String.valueOf(i));

                builder.redirectError(new File(test + "/error/servent" + i + "_err.txt"));
                builder.redirectInput(new File(test + "/input/servent" + i + "_in.txt"));
                builder.redirectOutput(new File(test + "/output/servent" + i + "_out.txt"));

                Process p = builder.start();
                processes.add(p);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Thread serventCLIThread = new Thread(new ServentCLI(processes));
        serventCLIThread.start();

        for (Process process : processes) {
            try {
                process.waitFor();  // Wait for graceful process finish.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Logger.timestampedStandardPrint("All servents processes finished. Type \"stop\" to exit.");

    }

}
