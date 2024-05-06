package sbu.cs;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RaceCondition {
    public static class Task implements Runnable {
        private int value;

        private Lock lock;
        public Task(int value, Lock lock) {
            this.value = value;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {

                // Using a Reentrant Lock
//                lock.lock();
//                x += this.value;
//                lock.unlock();

                // Synchronized Keyword
                addValue(this.value);

                // Atomic operation
                atomicX.addAndGet(this.value);
            }

            System.out.println(Thread.currentThread().getName() + " has stopped!");
        }

    }

    public static synchronized void addValue(int value) {
        x += value;
    }

    static int x;
    static AtomicInteger atomicX;

    public static void main(String[] args) {
        x = 0;

        atomicX = new AtomicInteger(0);

        Lock lock = new ReentrantLock();

        Task task1 = new Task(1, lock);
        Task task2 = new Task(-1, lock);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(x);
        System.out.println(atomicX.get());
    }
}
