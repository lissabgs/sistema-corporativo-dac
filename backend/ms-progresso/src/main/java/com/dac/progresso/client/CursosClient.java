package com.dac.progresso.client;

import com.dac.progresso.dto.CursoIntegrationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Aponta para o microsserviço de cursos
@FeignClient(name = "ms-cursos", url = "${app.feign.cursos-url:http://ms-cursos:8083}")
public interface CursosClient {

    @GetMapping("/api/cursos/{id}")
    CursoIntegrationDTO obterCursoPorId(@PathVariable("id") Long id);
}