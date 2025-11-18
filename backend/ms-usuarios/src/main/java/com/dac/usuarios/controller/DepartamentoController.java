package com.dac.usuarios.controller;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.repository.DepartamentoRepository; // Importe o repositório
import com.dac.usuarios.service.DepartamentoService;
import com.dac.usuarios.dto.DepartamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importe List

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @PostMapping
    public ResponseEntity<Departamento> criarDepartamento(@RequestBody DepartamentoDTO dto) {
        Departamento novoDepto = departamentoService.criarDepartamento(dto);
        return ResponseEntity.status(201).body(novoDepto);
    }

    // --- NOVO MÉTODO GET ---
    @GetMapping
    public ResponseEntity<List<Departamento>> listarDepartamentos() {
        List<Departamento> departamentos = departamentoRepository.findAll();
        return ResponseEntity.ok(departamentos);
    }
}