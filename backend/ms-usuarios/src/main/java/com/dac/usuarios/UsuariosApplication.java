package com.dac.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Importar
import org.springframework.web.client.RestTemplate; // Importar

@SpringBootApplication
public class UsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsuariosApplication.class, args);
    }

    // RestTemplate nos serviços
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}