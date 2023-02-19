package ru.gb.rabbit_mq.console.producers;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class TaskProducerApp {
    private static final String TASK_EXCHANGER = "task_exchanger";
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            String message = "Task.....";
            channel.exchangeDeclare(TASK_EXCHANGER, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < 20; i++) {
                channel.basicPublish(TASK_EXCHANGER, "java", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
