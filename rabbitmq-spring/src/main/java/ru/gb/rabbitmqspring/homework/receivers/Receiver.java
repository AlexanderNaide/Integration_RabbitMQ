package ru.gb.rabbitmqspring.homework.receivers;


import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    private final CountDownLatch latch = new CountDownLatch(3);

    public void receiveMessage(String message) {

        System.out.println(" [x] Received '" + message + "'");
        doWork(message);
        System.out.println(" [x] Done");

//        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
