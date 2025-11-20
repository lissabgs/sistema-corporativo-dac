package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.service.CorrecaoService;
import com.dac.avaliacoes.dto.CorrecaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/correcoes")
public class CorrecaoController {

    @Autowired
    private CorrecaoService correcaoService;

    // ========== CORRIGIR QUESTÃO ==========
    // ✅ APENAS INSTRUTOR + ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<CorrecaoDTO> corrigirQuestao(
            @RequestParam Long tentativaId,
            @RequestParam Long instrutorId,
            @RequestParam String feedback,
            @RequestParam Double nota) {
        CorrecaoDTO correcaoDTO = correcaoService.corrigirQuestao(tentativaId, instrutorId, feedback, nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(correcaoDTO);
    }

    // ========== VER MINHAS CORREÇÕES ==========
    // ✅ APENAS INSTRUTOR + ADMIN
    @GetMapping("/minhas-correcoes")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<CorrecaoDTO>> minhasCorrecoes(
            @RequestParam Long instrutorId) {
        List<CorrecaoDTO> correcoes = correcaoService.listarPorInstrutor(instrutorId);
        return ResponseEntity.ok(correcoes);
    }

    // ========== VER TODAS AS CORREÇÕES ==========
    //  APENAS ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CorrecaoDTO>> listarTodasCorrecoes() {
        List<CorrecaoDTO> correcoes = correcaoService.listarTodas();
        return ResponseEntity.ok(correcoes);
    }

    // ========== VER FEEDBACK DE MINHA TENTATIVA ==========
    //  APENAS FUNCIONÁRIO
    @GetMapping("/minhas-correcoes-recebidas/{tentativaId}")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<List<CorrecaoDTO>> minhasCorrecoesFuncionario(
            @PathVariable Long tentativaId) {
        List<CorrecaoDTO> correcoes = correcaoService.listarPorTentativa(tentativaId);
        return ResponseEntity.ok(correcoes);
    }
}
