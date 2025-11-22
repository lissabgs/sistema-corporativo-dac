package com.dac.usuarios.controller;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.service.DepartamentoService;
import com.dac.usuarios.dto.DepartamentoDTO;
import com.dac.usuarios.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @GetMapping
    public ResponseEntity<List<Departamento>> listar() {
        return ResponseEntity.ok(departamentoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Departamento> criar(@RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(201).body(departamentoService.criarDepartamento(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> atualizar(@PathVariable Long id, @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.ok(departamentoService.atualizarDepartamento(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletarDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}