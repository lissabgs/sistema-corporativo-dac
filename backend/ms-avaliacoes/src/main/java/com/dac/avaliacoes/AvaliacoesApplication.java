package com.dac.avaliacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class AvaliacoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(AvaliacoesApplication.class, args);
    }
}
