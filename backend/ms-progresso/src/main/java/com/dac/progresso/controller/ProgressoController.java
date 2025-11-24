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
        List<Progresso> progressos = progressoService.listarInscricoesDoAluno(funcionarioId);

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

    // --- ENDPOINTS CORRIGIDOS (Long -> String) ---

    @PutMapping("/iniciar/{id}")
    public ResponseEntity<Progresso> iniciarCurso(@PathVariable String id) { // String
        Progresso progresso = progressoService.iniciarCurso(id);
        return ResponseEntity.ok(progresso);
    }

    @PutMapping("/pausar/{id}")
    public ResponseEntity<Progresso> pausarCurso(@PathVariable String id) { // String
        Progresso progresso = progressoService.pausarCurso(id);
        return ResponseEntity.ok(progresso);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Progresso> cancelarInscricao(@PathVariable String id) { // String
        Progresso progresso = progressoService.cancelarInscricao(id);
        return ResponseEntity.ok(progresso);
    }

    // ---------------------------------------------

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Map<String, Object>>> buscarProgressoFuncionario(@PathVariable Long funcionarioId) {
        return listarMeusCursos(funcionarioId);
    }
}