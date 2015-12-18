package ru.fizteh.fivt.students.Jettriangle.threads;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

/**
 * Created by rtriangle on 18.12.15.
 */

public class BlockingQueueTests {
    public static class BlockingQueueTest {
        @Test
        public void testSimpleWithoutThreads() throws InterruptedException {
            BlockingQueue<Integer> queue = new BlockingQueue<Integer>(40);
            ArrayList<Integer> list1 = new ArrayList<Integer>();
            list1.add(100);
            list1.add(1000);
            list1.add(100000);
            ArrayList<Integer> list2 = new ArrayList<Integer>();
            list2.add(100);
            list2.add(1000);
            ArrayList<Integer> list3 = new ArrayList<Integer>();
            list3.add(11);
            list3.add(111);
            ArrayList<Integer> list4 = new ArrayList<Integer>();
            list4.add(1);
            list4.add(11);
            list4.add(111);
            queue.offer(list1);
            List answer1 = queue.take(100000);
            queue.offer(list3);
            List answer2 = queue.take(1);
            Assert.assertEquals(answer1, list2);
            Assert.assertEquals(answer2, list4);
        }

        @Test
        public void testSimpleWithThreads() throws InterruptedException {
            BlockingQueue<Integer> queue = new BlockingQueue<>(5);
            SimpleThread thread1 = new SimpleThread("take", 1, 0.05, 10, queue);
            SimpleThread thread2 = new SimpleThread("offer", 10, 0.001, 1, queue);
            thread1.start();
            thread2.start();
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        private static class SimpleThread extends Thread {
            private String action;
            private Integer times, value;
            private Double interval;
            private BlockingQueue queue;
            SimpleThread(String newAction, Integer newValue, Double newInterval, Integer newTimes, BlockingQueue newQueue) {
                interval = newInterval;
                value = newValue;
                action = newAction;
                times = newTimes;
                queue = newQueue;
            }

            @Override
            public void run() {
                Integer last = 0;
                for (int i = 0; i < times; ++i) {
                    try {
                        sleep((int) (1000 * interval));
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                    if (Objects.equals(action, "take")) {
                        List list = null;
                        try {
                            list = queue.take(value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int j = 0; j < value; ++j) {
                            ++last;
                            Assert.assertEquals(list.get(j), last);
                        }

                    } else {
                        ArrayList<Integer> list = new ArrayList<Integer>(value);
                        for (int j = 0; j < value; ++j) {
                            ++last;
                            list.add(last);
                        }
                        try {
                            queue.offer(list);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
