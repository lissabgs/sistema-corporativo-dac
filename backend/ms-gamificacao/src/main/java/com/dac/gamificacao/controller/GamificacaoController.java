package com.dac.gamificacao.controller;

import com.dac.gamificacao.model.Pontuacao;
import com.dac.gamificacao.repository.PontuacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gamificacao")
public class GamificacaoController {

    @Autowired
    private PontuacaoRepository pontuacaoRepository;

    /**
     * Endpoint para buscar pontuação de um funcionário
     * GET /api/gamificacao/funcionario/{funcionarioId}
     */
    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<Map<String, Object>> buscarPontuacao(@PathVariable Long funcionarioId) {

        Pontuacao pontuacao = pontuacaoRepository.findByFuncionarioId(funcionarioId)
                .orElse(new Pontuacao(funcionarioId));

        Map<String, Object> response = new HashMap<>();
        response.put("funcionarioId", pontuacao.getFuncionarioId());
        response.put("xpTotal", pontuacao.getXpTotal());
        response.put("nivel", pontuacao.getNivel());
        response.put("badges", pontuacao.getBadges());

        return ResponseEntity.ok(response);
    }
}
