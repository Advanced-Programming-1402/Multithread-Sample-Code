package sbu.cs;

import static java.lang.Thread.sleep;

public class BasicSyntax
{
    public static class MyTask implements Runnable
    {
        int sleepDuration;
        public MyTask(int sleepDuration) {
            this.sleepDuration = sleepDuration;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " has started.");

            try {
                Thread.sleep(sleepDuration);
                System.out.println(Thread.currentThread().getName() + " has stopped.");
            }
            catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted.");
            }
        }
    }

    public static class MyThread extends Thread {
        public MyThread() {
            super();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("stuck in a loop!");
                    sleep(100);
                }
            }
            catch (InterruptedException e) {
                System.out.println("goodbye world!!");
                System.out.println(e.getMessage());
            }
            finally {
                System.out.println("goodbye world forever!!");
            }

//            while (true) {
//                if (this.isInterrupted()) {
//                    break;
//                }
//            }
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Test 1:");
        MyThread myThread = new MyThread();
        myThread.start();

        try {
            Thread.sleep(1000);
            myThread.interrupt();
            myThread.join();
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("\nTest 2:");
        MyTask myTask1 = new MyTask(3000);
        MyTask myTask2 = new MyTask(5000);
        Thread thread1 = new Thread(myTask1);
        Thread thread2 = new Thread(myTask2);

        thread1.start();

        try {
            thread1.join();
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        thread2.start();

    }

}
