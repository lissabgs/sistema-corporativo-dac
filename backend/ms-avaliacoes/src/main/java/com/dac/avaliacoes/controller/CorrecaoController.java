package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.service.CorrecaoService;
import com.dac.avaliacoes.dto.CorrecaoDTO;
import com.dac.avaliacoes.dto.CorrecaoRequestDTO; // Certifique-se de importar o novo DTO
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

    // ========== CORRIGIR QUESTÃO (Método Antigo) ==========
    // APENAS INSTRUTOR + ADMIN
    @PostMapping("/questao") // Mudei levemente a rota para não conflitar com o novo POST raiz, ou mantenha se a assinatura for diferente
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<CorrecaoDTO> corrigirQuestao(
            @RequestParam Long tentativaId,
            @RequestParam Long instrutorId,
            @RequestParam String feedback,
            @RequestParam Double nota) {
        CorrecaoDTO correcaoDTO = correcaoService.corrigirQuestao(tentativaId, instrutorId, feedback, nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(correcaoDTO);
    }

    // ========== VER MINHAS CORREÇÕES (Método Antigo) ==========
    // APENAS INSTRUTOR + ADMIN
    @GetMapping("/minhas-correcoes")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<CorrecaoDTO>> minhasCorrecoes(
            @RequestParam Long instrutorId) {
        List<CorrecaoDTO> correcoes = correcaoService.listarPorInstrutor(instrutorId);
        return ResponseEntity.ok(correcoes);
    }

    // ========== VER TODAS AS CORREÇÕES (Método Antigo) ==========
    //  APENAS ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CorrecaoDTO>> listarTodasCorrecoes() {
        List<CorrecaoDTO> correcoes = correcaoService.listarTodas();
        return ResponseEntity.ok(correcoes);
    }

    // ========== VER FEEDBACK DE MINHA TENTATIVA (Método Antigo) ==========
    //  APENAS FUNCIONÁRIO
    @GetMapping("/minhas-correcoes-recebidas/{tentativaId}")
    @PreAuthorize("hasRole('FUNCIONARIO')")
    public ResponseEntity<List<CorrecaoDTO>> minhasCorrecoesFuncionario(
            @PathVariable Long tentativaId) {
        List<CorrecaoDTO> correcoes = correcaoService.listarPorTentativa(tentativaId);
        return ResponseEntity.ok(correcoes);
    }

    // ========== NOVO ENDPOINT: SALVAR CORREÇÃO COMPLETA ==========
    // Usado pela nova tela de correção do Front-end
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> salvarCorrecao(@RequestBody CorrecaoRequestDTO dto) {
        correcaoService.salvarCorrecao(dto);
        return ResponseEntity.ok().build();
    }
}