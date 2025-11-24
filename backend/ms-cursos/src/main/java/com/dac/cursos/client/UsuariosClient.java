package com.dac.cursos.client;

import com.dac.cursos.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Aponta para o MS-USUARIOS na porta 8082
@FeignClient(name = "ms-usuarios", url = "${app.feign.usuarios-url:http://ms-usuarios:8082}")
public interface UsuariosClient {

    @GetMapping("/api/funcionarios/{id}")
    UsuarioDTO buscarPorId(@PathVariable("id") Long id);
}