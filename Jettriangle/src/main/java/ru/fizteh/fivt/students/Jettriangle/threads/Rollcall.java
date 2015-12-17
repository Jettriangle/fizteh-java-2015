package ru.fizteh.fivt.students.Jettriangle.threads;

/**
 * Created by rtriangle on 17.12.15.
 */

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Rollcall {
    public static class ParameterException extends Exception {
        public ParameterException(String message) {
            super(message);
        }
    }

    private static volatile boolean yesEveryThread = false;
    private static CyclicBarrier threadsToAsk, waitingAnswers;

    public static void main(String[] args) {
        Integer n = 0;
        try {
            if (args.length != 1) {
                throw new ParameterException("Argument must be exactly one positive number");
            }
            n = Integer.valueOf(args[0]);
            if (n <= 0) {
                throw new ParameterException("Argument must be positive");
            }
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        RollcallThread[] threads = new RollcallThread[n];
        threadsToAsk = new CyclicBarrier(n + 1);
        waitingAnswers = new CyclicBarrier(n + 1);

        for (int i = 0; i < n; i++) {
            threads[i] = new RollcallThread();
            threads[i].start();
        }

        while (!yesEveryThread) {
            System.out.println("Are you ready?");
            yesEveryThread = true;
            try {
                threadsToAsk.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            threadsToAsk.reset();
            try {
                waitingAnswers.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            waitingAnswers.reset();
            if (yesEveryThread) {
                System.exit(0);
            }
        }
    }

    private static class RollcallThread extends Thread {
        private Boolean result;
        private Random rand = new Random();

        @Override
        public void run() {
            while (true) {
                try {
                    threadsToAsk.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                result = rand.nextInt(10) != 0;
                if (result) {
                    System.out.println("Yes");
                } else {
                    System.out.println("No");
                }
                if (!result) {
                    yesEveryThread = false;
                }
                try {
                    waitingAnswers.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }
}