package com.dac.avaliacoes.controller;
import com.dac.avaliacoes.dto.CursoDTO;
import com.dac.avaliacoes.dto.*;
import com.dac.avaliacoes.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService; // <--- Só pode ter UM destes

    // ========== CRIAR AVALIAÇÃO ==========
    //  APENAS INSTRUTOR + ADMIN
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(@RequestBody CriarAvaliacaoRequestDTO dto) {
        return ResponseEntity.ok(avaliacaoService.criarAvaliacao(dto));
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listarTodas() {
        return ResponseEntity.ok(avaliacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @GetMapping("/cursos-disponiveis")
    public ResponseEntity<List<Object>> listarCursosSemAvaliacao() {
        // Chame o método com o nome CORRETO definido no Service
        List<Object> cursos = avaliacaoService.buscarCursosSemAvaliacao();
        return ResponseEntity.ok(cursos);
    }
    @GetMapping("/cursos-sem-avaliacao")
    public ResponseEntity<List<Object>> buscarCursosSemAvaliacao() {
        return ResponseEntity.ok(avaliacaoService.buscarCursosSemAvaliacao());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> atualizarAvaliacao(@PathVariable Long id, @RequestBody CriarAvaliacaoRequestDTO dto) {
        return ResponseEntity.ok(avaliacaoService.atualizarAvaliacao(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DO ALUNO (NOVOS) ---
    @GetMapping("/funcionario/{id}/pendentes")
    public ResponseEntity<List<AvaliacaoDTO>> listarPendentes(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarPendentes(id));
    }

    @GetMapping("/funcionario/{id}/aguardando")
    public ResponseEntity<List<TentativaDTO>> listarAguardando(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarAguardandoCorrecao(id));
    }

    @GetMapping("/funcionario/{id}/concluidas")
    public ResponseEntity<List<TentativaDTO>> listarConcluidas(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarConcluidas(id));
    }
}