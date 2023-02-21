package ru.gb.rabbitmqspring.homework;


import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.gb.rabbitmqspring.homework.receivers.Receiver;

@SpringBootApplication
public class SpringIntegrationApp {

    static final String topicExchangeName = "task_exchanger";

    static final String queueName = "task_queue";

    @Bean
    Queue queue() {
        return new Queue(queueName, false, false, false, null);
    }

    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(topicExchangeName);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("java");
    }

//    @Bean
//    Binding binding(Queue queue, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange);
//    }

//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("");
//    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationApp.class, args);

    }


}