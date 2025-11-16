package com.dac.gamificacao;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class GamificacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamificacaoApplication.class, args);
    }

}