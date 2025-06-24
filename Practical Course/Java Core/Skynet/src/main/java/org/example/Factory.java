package org.example;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Factory implements Runnable {
    private static final int DAYS = 100;
    private static final int MAX_PARTS = 5;
    public static final int TYPES_COUNT = 4;
    private static final int MAX_PRODUCTIVITY = 10;

    private final int[] parts = new int[4];
    private final Lock lock = new ReentrantLock();
    private final Condition productionDone = lock.newCondition();
    private final Condition collectionDone = lock.newCondition();

    private boolean productionCompleted = false;
    private int collectorsCount = 0;
    private volatile boolean running = true;
    private final Random random = new Random();

    @Override
    public void run() {
        try {
            for (int i = 0; i < DAYS && running; i++) {
                lock.lock();
                try {
                    produceParts();
                    productionCompleted = true;
                    productionDone.signalAll();

                    while (collectorsCount < 2) {
                        collectionDone.await();
                    }
                    collectorsCount = 0;
                    productionCompleted = false;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            running = false;
            lock.lock();
            try {
                productionDone.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    private void produceParts() {
        int partsCount = random.nextInt(1, MAX_PRODUCTIVITY + 1);
        for (int i = 0; i < partsCount; i++) {
            parts[random.nextInt(TYPES_COUNT)]++;
        }
    }

    public void collectParts(Faction faction) {
        lock.lock();
        try {
            while (!productionCompleted && running) {
                productionDone.await();
            }

            if (!running) return;

            int partsCount = 0;
            while (partsCount < MAX_PARTS) {
                int partType = -1;
                for (int i = 0; i < TYPES_COUNT; i++) {
                    if (parts[i] > 0) {
                        partType = i;
                        break;
                    }
                }

                if (partType == -1) break;

                parts[partType]--;
                faction.addPart(partType);
                partsCount++;
            }

            collectorsCount++;
            if (collectorsCount == 2) {
                collectionDone.signal();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public boolean isRunning() {
        return running;
    }
}