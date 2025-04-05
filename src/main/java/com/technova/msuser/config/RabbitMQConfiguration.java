package com.technova.msuser.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConfiguration {
    @Bean
    public Queue queueUserSaveRequest() {
        return new Queue("user-save-request", true);
    }

    @Bean
    public Exchange exchangeUserSaveRequest() {
        return new TopicExchange("user-save-request-exchange", true, false);
    }

    @Bean
    public Queue queueUserLoginRequest() {
        return new Queue("user-login-request", true);
    }

    @Bean
    public Exchange exchangeUserLoginRequest() {
        return new TopicExchange("user-login-request-exchange", true, false);
    }

    @Bean
    public Queue queueUserFindByEmailRequest() {
        return new Queue("user-find-by-email-request", true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
