package com.dac.notificacoes.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter; // Import novo
import org.springframework.amqp.support.converter.MessageConverter;             // Import novo
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE = "email.queue";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    // --- A MÁGICA ACONTECE AQUI ---
    // Esse Bean ensina o Spring a converter JSON <-> Objeto Java automaticamente
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}