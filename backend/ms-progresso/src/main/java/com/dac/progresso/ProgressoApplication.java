package com.dac.progresso;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit // <-- ADICIONAR ESTA ANOTAÇÃO
public class ProgressoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgressoApplication.class, args);
    }

}