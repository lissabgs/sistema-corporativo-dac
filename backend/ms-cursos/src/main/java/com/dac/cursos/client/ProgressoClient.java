package com.dac.cursos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ms-progresso", url = "http://ms-progresso:8085")
public interface ProgressoClient {

    @GetMapping("/api/progresso/matriculados/{funcionarioId}")
    List<String> obterCodigosMatriculados(@PathVariable("funcionarioId") Long funcionarioId);
}