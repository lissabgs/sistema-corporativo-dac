package com.dac.avaliacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para comunicação com o MS-PROGRESSO
 * URL base: http://ms-progresso:8085
 */
@FeignClient(name = "ms-progresso", url = "http://ms-progresso:8085")
public interface ProgressoClient {

    @GetMapping("/api/progresso/funcionario/{funcionarioId}")
    List<Map<String, Object>> buscarProgressoFuncionario(@PathVariable Long funcionarioId);
}
