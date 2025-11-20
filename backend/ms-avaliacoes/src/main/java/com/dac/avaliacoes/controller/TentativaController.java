package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.service.TentativaService;
import com.dac.avaliacoes.model.Tentativa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tentativas")
public class TentativaController {

    @Autowired
    private TentativaService tentativaService;

    // ========== INICIAR TENTATIVA ==========
    //  FUNCIONÁRIO + INSTRUTOR (para testar)
    @PostMapping("/iniciar")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'INSTRUTOR')")
    public ResponseEntity<Tentativa> iniciarTentativa(
            @RequestParam Long funcionarioId,
            @RequestParam Long avaliacaoId) {
        Tentativa tentativa = tentativaService.iniciarTentativa(funcionarioId, avaliacaoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tentativa);
    }

    // ========== FINALIZAR TENTATIVA ==========
    //  FUNCIONÁRIO + INSTRUTOR
    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'INSTRUTOR')")
    public ResponseEntity<String> finalizarTentativa(
            @PathVariable Long id,
            @RequestParam Double notaFinal) {
        tentativaService.finalizarTentativa(id, notaFinal);
        return ResponseEntity.ok("Tentativa finalizada com sucesso");
    }

    // ========== VER MINHAS TENTATIVAS ==========
    // APENAS FUNCIONÁRIO
    @GetMapping("/meus-resultados")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<List<Tentativa>> meusPropiosResultados(
            @RequestParam Long funcionarioId) {
        List<Tentativa> tentativas = tentativaService.listarPorFuncionario(funcionarioId);
        return ResponseEntity.ok(tentativas);
    }

    // ========== VER TODAS TENTATIVAS (por Avaliação) ==========
    //  APENAS INSTRUTOR + ADMIN
    @GetMapping("/avaliacao/{avaliacaoId}")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<Tentativa>> listarTentativasPorAvaliacao(
            @PathVariable Long avaliacaoId) {
        List<Tentativa> tentativas = tentativaService.listarPorAvaliacao(avaliacaoId);
        return ResponseEntity.ok(tentativas);
    }
}