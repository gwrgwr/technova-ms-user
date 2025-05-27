package com.technova.msuser.config;

import com.technova.user.constants.RabbitUserConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConfiguration {

    @Bean
    public Exchange userExchange() {
        return new DirectExchange(RabbitUserConstants.USER_EXCHANGE, true, false);
    }

    @Bean
    public Queue queueUserSaveRequest() {
        return new Queue(RabbitUserConstants.USER_SAVE_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue queueUserLoginRequest() {
        return new Queue(RabbitUserConstants.USER_LOGIN_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue queueUserFindByIdRequest() {
        return new Queue(RabbitUserConstants.USER_FIND_BY_ID_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue queueUserDeleteRequest() {
        return new Queue(RabbitUserConstants.USER_DELETE_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue queueUserUpdateRequest() {
        return new Queue(RabbitUserConstants.USER_UPDATE_REQUEST_QUEUE, true);
    }

    @Bean
    public Binding bindingUserUpdateRequest(Queue queueUserUpdateRequest, Exchange userExchange) {
        return BindingBuilder.bind(queueUserUpdateRequest).to(userExchange).with(RabbitUserConstants.USER_UPDATE_REQUEST_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingUserDeleteRequest(Queue queueUserDeleteRequest, Exchange userExchange) {
        return BindingBuilder.bind(queueUserDeleteRequest).to(userExchange).with(RabbitUserConstants.USER_DELETE_REQUEST_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingUserSaveRequest(Queue queueUserSaveRequest, Exchange userExchange) {
        return BindingBuilder.bind(queueUserSaveRequest).to(userExchange).with(RabbitUserConstants.USER_SAVE_REQUEST_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingUserLoginRequest(Queue queueUserLoginRequest, Exchange userExchange) {
        return BindingBuilder.bind(queueUserLoginRequest).to(userExchange).with(RabbitUserConstants.USER_LOGIN_REQUEST_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingUserFindByIdRequest(Queue queueUserFindByIdRequest, Exchange userExchange) {
        return BindingBuilder.bind(queueUserFindByIdRequest).to(userExchange).with(RabbitUserConstants.USER_FIND_BY_ID_REQUEST_ROUTING_KEY).noargs();
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
