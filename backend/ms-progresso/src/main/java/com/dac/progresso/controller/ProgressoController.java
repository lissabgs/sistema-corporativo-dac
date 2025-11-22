package com.dac.progresso.controller;

import com.dac.progresso.model.Progresso;
import com.dac.progresso.service.ProgressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; // Import

@RestController
@RequestMapping("/api/progresso")
public class ProgressoController {

    @Autowired
    private ProgressoService progressoService;

    @PostMapping("/matricular")
    public ResponseEntity<Progresso> matricular(
            @RequestParam Long funcionarioId,
            @RequestParam String cursoId) {
        
        Progresso progresso = progressoService.matricularAluno(funcionarioId, cursoId);
        return ResponseEntity.ok(progresso);
    }

    // NOVO: Endpoint para o frontend saber o que o aluno já cursa
    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Progresso>> listarPorFuncionario(@PathVariable Long funcionarioId) {
        List<Progresso> lista = progressoService.listarInscricoesDoAluno(funcionarioId);
        return ResponseEntity.ok(lista);
    }
}