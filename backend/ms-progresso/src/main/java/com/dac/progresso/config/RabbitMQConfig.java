package com.dac.progresso.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Define um nome padrão para a fila de XP
    public static final String XP_QUEUE_NAME = "xp.aula.concluida.queue";

    @Bean
    public Queue xpQueue() {
        // durable = true (a fila sobrevive a reinícios do RabbitMQ)
        return new Queue(XP_QUEUE_NAME, true);
    }
}