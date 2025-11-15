package com.dac.gamificacao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String XP_QUEUE_NAME = "xp.aula.concluida.queue";

    @Bean
    public Queue xpQueue() {
        return new Queue(XP_QUEUE_NAME, true);
    }
}