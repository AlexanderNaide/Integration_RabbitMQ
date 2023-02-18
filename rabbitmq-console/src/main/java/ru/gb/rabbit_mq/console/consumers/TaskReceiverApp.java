package ru.gb.rabbit_mq.console.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TaskReceiverApp {
    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String TASK_EXCHANGER = "task_exchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        channel.queueBind(TASK_QUEUE_NAME, TASK_EXCHANGER, "");
        System.out.println(" [*] Waiting for messages");

        channel.basicQos(2); // параметр, который устанавливает по сколько задач receiver может автоматически забирать из очереди,
        // иначе по умолчанию заберет много (10)

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            doWork(message);
            System.out.println(" [x] Done");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // такой командой происходит подтверждение успешной обработки сообщения
        };

        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            // если второй аргумент false - ждем подтверждения успешного выполнения задачи
            // если true - задача, которую забрали из очереди, автоматически удаляется без подтверждения
        });
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
}
