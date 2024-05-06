package sbu.cs;

import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumer {

    public static class Producer extends Thread {
        ArrayBlockingQueue<Integer> queue;  // this queue will be a bridge between the producer and the consumer
        public Producer(ArrayBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static class Consumer extends Thread {
        ArrayBlockingQueue<Integer> queue;  // this queue will be a bridge between the producer and the consumer
        public Consumer(ArrayBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer x = queue.take();
                    System.out.println(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
