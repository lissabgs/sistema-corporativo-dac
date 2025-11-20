package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.service.AvaliacaoService;
import com.dac.avaliacoes.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // ========== CRIAR AVALIAÇÃO ==========
    //  APENAS INSTRUTOR + ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(@RequestBody CriarAvaliacaoRequestDTO dto) {
        AvaliacaoDTO avaliacaoDTO = avaliacaoService.criarAvaliacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoDTO);
    }

    // ========== LISTAR TODAS ==========
    // TODOS autenticados
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AvaliacaoDTO>> listarAvaliacoes() {
        List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarTodas();
        return ResponseEntity.ok(avaliacoes);
    }

    // ========== BUSCAR POR ID ==========
    // TODOS autenticados
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AvaliacaoDTO> buscarAvaliacao(@PathVariable Long id) {
        AvaliacaoDTO avaliacaoDTO = avaliacaoService.buscarPorId(id);
        return ResponseEntity.ok(avaliacaoDTO);
    }

    // ========== ATUALIZAR ==========
    //  APENAS INSTRUTOR + ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<AvaliacaoDTO> atualizarAvaliacao(
            @PathVariable Long id,
            @RequestBody CriarAvaliacaoRequestDTO dto) {
        AvaliacaoDTO avaliacaoDTO = avaliacaoService.atualizarAvaliacao(id, dto);
        return ResponseEntity.ok(avaliacaoDTO);
    }

    // ========== DELETAR ==========
    //  APENAS ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> deletarAvaliacao(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.ok("Avaliação deletada com sucesso");
    }
}
