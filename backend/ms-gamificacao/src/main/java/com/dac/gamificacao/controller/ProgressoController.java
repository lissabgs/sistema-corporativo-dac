package com.dac.progresso.controller;

import com.dac.progresso.model.Progresso;
import com.dac.progresso.repository.ProgressoRepository;
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

    /**
     * Endpoint para buscar progresso de um funcionário em todos os cursos
     * GET /api/progresso/funcionario/{funcionarioId}
     */
    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Map<String, Object>>> buscarProgressoFuncionario(
            @PathVariable Long funcionarioId) {

        List<Progresso> progressos = progressoRepository.findAll().stream()
                .filter(p -> p.getFuncionarioId().equals(funcionarioId))
                .collect(Collectors.toList());

        List<Map<String, Object>> response = progressos.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("funcionarioId", p.getFuncionarioId());
                    map.put("cursoId", p.getCursoId());
                    map.put("aulasConcluidas", p.getAulasConcluidas());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
