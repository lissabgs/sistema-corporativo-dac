package com.dac.avaliacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;


@FeignClient(name = "ms-cursos", url = "http://ms-cursos:8083")
public interface CursosClient {

    @GetMapping("/api/cursos/{id}")
    Map<String, Object> buscarCurso(@PathVariable("id") String id);

    // ADICIONE ESTE MÉTODO QUE FALTAVA
    @GetMapping("/api/cursos")
    List<Object> listarCursos();
}