package com.dac.avaliacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Cliente Feign para comunicação com o MS-USUARIOS
 * URL base: http://ms-usuarios:8082
 */
@FeignClient(name = "ms-usuarios", url = "http://ms-usuarios:8082")
public interface UsuariosClient {

    @GetMapping("/api/funcionarios/{id}")
    Map<String, Object> buscarFuncionario(@PathVariable Long id);
}