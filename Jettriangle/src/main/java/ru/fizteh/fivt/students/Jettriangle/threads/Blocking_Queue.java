package ru.fizteh.fivt.students.Jettriangle.threads;

/**
 * Created by rtriangle on 17.12.15.
 */

import java.util.*;

class BlockingQueue<T> {

    private Queue<T> q;
    private final int maxQueueSize;

    BlockingQueue(int maxSize) {
        q = new LinkedList<>();
        maxQueueSize = maxSize;
    }

    synchronized List<T> take(int size) throws InterruptedException {
        while (q.size() < size) {
            wait();
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            result.add(q.remove());
        }
        notifyAll();
        return result;
    }

    synchronized List<T> take(int n, long timeout) throws InterruptedException {
        long timeLeft = timeout;
        long currentTime = System.currentTimeMillis();
        while (0 < timeLeft && q.size() < n) {
            wait(timeLeft);
            long actualCurrentTime = System.currentTimeMillis();
            timeLeft = timeLeft - (actualCurrentTime - currentTime);
            currentTime = actualCurrentTime;
        }
        if (timeLeft <= 0) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            result.add(q.remove());
        }
        return result;
    }

    synchronized void offer(List<T> e) throws InterruptedException {
        while (maxQueueSize < q.size() + e.size()) {
            wait();
        }
        q.addAll(e);
        notifyAll();
    }

    synchronized void offer(List<T> e, long timeout)
            throws InterruptedException {
        long timeLeft = timeout;
        long currentTime = System.currentTimeMillis();
        while (q.size() + e.size() > maxQueueSize && timeLeft > 0) {
            wait(timeLeft);
            long actualCurrentTime = System.currentTimeMillis();
            timeLeft -= (actualCurrentTime - currentTime);
            currentTime = actualCurrentTime;
        }
        if (timeLeft <= 0) {
            return;
        }
        q.addAll(e);
        notifyAll();
    }
}