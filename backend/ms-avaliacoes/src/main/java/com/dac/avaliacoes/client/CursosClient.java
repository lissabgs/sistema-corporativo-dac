package com.dac.avaliacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Cliente Feign para comunicação com o MS-CURSOS
 * URL base: http://ms-cursos:8083
 */
@FeignClient(name = "ms-cursos", url = "http://ms-cursos:8083")
public interface CursosClient {

    @GetMapping("/api/cursos/{id}")
    Map<String, Object> buscarCurso(@PathVariable String id);
}
