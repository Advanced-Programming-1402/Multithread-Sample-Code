package sbu.cs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

public class Deadlock {

    public static class Task implements Runnable {
        Lock lock_1, lock_2;
        public Task(Lock lock_1, Lock lock_2) {
            this.lock_1 = lock_1;
            this.lock_2 = lock_2;
        }

        public void run() {
            lock_1.lock();

                System.out.println(Thread.currentThread().getName() + " has acquired a lock: " + lock_1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {e.printStackTrace();}

                lock_2.lock();
                    System.out.println("This line will only print if no deadlock occurs!");
                lock_2.unlock();

            lock_1.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock_1 = new ReentrantLock();
        Lock lock_2 = new ReentrantLock();

        // Notice how task_2 received the swapped version of the locks
        Task task_1 = new Task(lock_1, lock_2);
        Task task_2 = new Task(lock_2, lock_1);

        ExecutorService tp = Executors.newFixedThreadPool(2);

        tp.execute(task_1);
        tp.execute(task_2);

    }
}
