package com.dac.progresso;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableRabbit // <-- ADICIONAR ESTA ANOTAÇÃO
@EnableFeignClients // <--- ADICIONE ISTO
public class ProgressoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgressoApplication.class, args);
    }

}