package ru.fizteh.fivt.students.Jettriangle.threads;

/**
 * Created by rtriangle on 17.12.15.
 */

public class Counter {
    public static volatile int printId;

    public static class ParameterException extends Exception {
        public ParameterException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        Integer n = 0;
        try {
            if(args.length != 1) {
                throw new ParameterException("Argument must be exactly one positive number");
            }
            n = Integer.valueOf(args[0]);
            if(n <= 0) {
                throw new ParameterException("Argument must be positive");
            }
        } catch (ParameterException e) {
            e.printStackTrace();
            System.exit(1);
        }
        printId = 0;
        for(int id = 0; id < n; id++) {
            CounterThread thread = new CounterThread(id, (id+1) % n);
            thread.start();
        }
    }

    private static Object monitor = new Object();

    private static class CounterThread extends Thread {
        private Integer myId, nextId;

        CounterThread(Integer id1, Integer id2) {
            myId = id1;
            nextId = id2;
        }

        @Override
        public void run() {
            while(true) {
                synchronized (monitor) {
                    while (myId != printId) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                            System.exit(1);
                        }
                    }
                    System.out.println("Thread-" + String.valueOf(nextId));
                    printId = nextId;
                    monitor.notifyAll();
                }
            }
        }
    }
}
