package ru.gb.rabbit_mq.console.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SimpleReceiverApp {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory(); // фабрика соединений с RabbitMQ
        factory.setHost("localhost"); // установили куда подключаться
        Connection connection = factory.newConnection(); //открываем соединение
        Channel channel = connection.createChannel(); // открываем канал

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //Объявляем очередь:
        // очередь именованная (первый параметр), если абстрактная временная, то queueDeclare() без параметров
        // durable (второй параметр) - долговечность, т.е. будет сохраняться при перезапуске сервера
        // exclusive (третий параметр) - если true, то к данной очереди кроме данного обработчика никакой другой подключиться не сможет
        // autoDelete (четвертый параметр) - если true, то сервер автоматически удалит очередь после отключения обработчика
        // arguments (пятый) - Map<String, Object> arguments - прочие аргументы
        // ВАЖНО! Если такая очередь есть, то эта команда пропустится, НО если такая очередь есть с другими параметрами - то прилетит Ошибка.

        System.out.println(" [*]  Waiting for message");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [*] Received '" + message + "'");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag ->{

        });
    }
}
