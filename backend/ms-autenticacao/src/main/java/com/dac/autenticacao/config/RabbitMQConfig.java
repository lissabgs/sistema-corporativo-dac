package com.dac.autenticacao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE = "email.queue";

    @Bean
    public Queue emailQueue() {
        // 'durable = true' garante que a fila sobreviva se o RabbitMQ reiniciar
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Isso ajuda a evitar erros se o pacote da classe DTO for diferente entre os microsserviços
        converter.setCreateMessageIds(true);
        return converter;
    }
}