package sbu.cs;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*
Example: find the sum of all elements in an array with a multithreading.
In this example, we will randomly generate numbers to simulate a large array.
*/


public class ArraySum {
    public static class Task implements Runnable {
        /*
        Each instance of this class generates an array with a number of
        random elements and then calculates the sum of all the elements.
        The summation is stored in `sum` and can later be accessed.
         */
        int sum, blockSize;
        public Task(int blockSize) {
            this.sum = 0;
            this.blockSize = blockSize;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is Active.");

            // Note that this code can be simplified as there is no need to store the numbers in an array.
            int[] array = new int[this.blockSize];
            ThreadLocalRandom rand = ThreadLocalRandom.current();

            for (int i = 0; i < this.blockSize; i++) {
                array[i] = rand.nextInt(-100, 101);  // generate random numbers to fill the array
            }

            for (int x: array) { // find the sum of all elements
                this.sum += x;
            }
        }
    }
    public static final int ARRAY_SIZE = 500000000;
    public static final int THREAD_COUNT = 16;   // Change the number, see what happens!

    public static void main(String[] args)
    {
        Instant start = Instant.now();

        int blockSize = (int) Math.floor(ARRAY_SIZE/THREAD_COUNT); // find the size of each block

        List<Task> tasksList = new ArrayList<>();
        List<Thread> threadsList = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {  // create tasks and threads
            Task task = new Task(blockSize);
            Thread thread = new Thread(task);

            tasksList.add(task);
            threadsList.add(thread);
        }

        for (Thread thread:threadsList) {  // start all threads
            thread.start();
        }

        for (Thread thread:threadsList) { // wait for all threads to join
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        int sum = 0;
        for (Task task:tasksList) {  // calculate the final sum
            sum += task.sum;
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        System.out.println("Number of Threads: " + THREAD_COUNT);
        System.out.println("sum: " + sum);
        System.out.println("Duration: " + duration.toMillis() + " ms");
    }

}
