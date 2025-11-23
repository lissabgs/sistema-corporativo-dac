package com.dac.progresso.controller;

import com.dac.progresso.model.Progresso;
import com.dac.progresso.repository.ProgressoRepository;
import com.dac.progresso.service.ProgressoService; // <--- 1. Importar o Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progresso")
public class ProgressoController {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private ProgressoService progressoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Map<String, Object>>> buscarProgressoFuncionario(
            @PathVariable Long funcionarioId) {

        List<Progresso> progressos = progressoRepository.findAll().stream()
                .filter(p -> p.getFuncionarioId().equals(funcionarioId))
                .collect(Collectors.toList());

        List<Map<String, Object>> response = progressos.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getId()); // É bom retornar o ID do registro
                    map.put("funcionarioId", p.getFuncionarioId());
                    map.put("cursoId", p.getCursoId());
                    map.put("status", p.getStatus());
                    map.put("aulasConcluidas", p.getAulasConcluidas());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/matricular")
    public ResponseEntity<Progresso> matricular(
            @RequestParam Long funcionarioId,
            @RequestParam String cursoId) {

        Progresso progresso = progressoService.matricularAluno(funcionarioId, cursoId);
        return ResponseEntity.ok(progresso);
    }
}