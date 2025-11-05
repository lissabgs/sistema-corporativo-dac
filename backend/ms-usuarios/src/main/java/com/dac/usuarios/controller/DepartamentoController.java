package com.dac.usuarios.controller;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.service.DepartamentoService;
import com.dac.usuarios.dto.DepartamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<Departamento> criarDepartamento(@RequestBody DepartamentoDTO dto) {
        Departamento novoDepto = departamentoService.criarDepartamento(dto);
        return ResponseEntity.status(201).body(novoDepto);
    }
}