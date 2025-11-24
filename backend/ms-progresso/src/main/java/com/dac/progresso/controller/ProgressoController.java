package com.dac.progresso.controller;

import com.dac.progresso.model.Progresso;
import com.dac.progresso.dto.MatriculaRequestDTO;
import com.dac.progresso.repository.ProgressoRepository;
import com.dac.progresso.service.ProgressoService;
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

    @GetMapping("/meus-cursos/{funcionarioId}")
    public ResponseEntity<List<Map<String, Object>>> listarMeusCursos(@PathVariable Long funcionarioId) {

        // Busca tudo que tem o ID do funcionário
        List<Progresso> progressos = progressoRepository.findAll().stream()
                .filter(p -> p.getFuncionarioId().equals(funcionarioId))
                .collect(Collectors.toList());

        // Monta a resposta
        List<Map<String, Object>> response = progressos.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getId());
                    map.put("funcionarioId", p.getFuncionarioId());
                    map.put("cursoId", p.getCursoId());
                    map.put("status", p.getStatus());
                    map.put("aulasConcluidas", p.getAulasConcluidas());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    // -----------------------------

    // Mantenha os outros métodos existentes abaixo (matricular, etc...)
    @PostMapping("/matricular")
    public ResponseEntity<Progresso> matricularAluno(@RequestBody MatriculaRequestDTO dto) {
        if (dto.getFuncionarioId() == null || dto.getCursoId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Progresso novoProgresso = progressoService.matricularAluno(dto.getFuncionarioId(), dto.getCursoId());
        return ResponseEntity.ok(novoProgresso);
    }

    @GetMapping("/matriculados/{funcionarioId}")
    public ResponseEntity<List<String>> obterCodigosMatriculados(@PathVariable Long funcionarioId) {
        List<String> codigos = progressoService.listarCodigosMatriculados(funcionarioId);
        return ResponseEntity.ok(codigos);
    }
}