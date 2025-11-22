package com.dac.avaliacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Cliente Feign para comunicação com o MS-GAMIFICACAO
 * URL base: http://ms-gamificacao:8086
 */
@FeignClient(name = "ms-gamificacao", url = "http://ms-gamificacao:8086")
public interface GamificacaoClient {

    @GetMapping("/api/gamificacao/funcionario/{funcionarioId}")
    Map<String, Object> buscarPontuacao(@PathVariable Long funcionarioId);
}
