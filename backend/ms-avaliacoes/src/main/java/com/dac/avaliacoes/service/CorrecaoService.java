package com.dac.avaliacoes.service;

import com.dac.avaliacoes.dto.CorrecaoDTO;
import com.dac.avaliacoes.dto.CorrecaoRequestDTO; // Import do novo DTO
import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorrecaoService {

    private static final Logger logger = LoggerFactory.getLogger(CorrecaoService.class);

    @Autowired
    private CorrecaoRepository correcaoRepository;

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    // ========== SALVAR CORREÇÃO COMPLETA (Novo Método) ==========
    @Transactional
    public void salvarCorrecao(CorrecaoRequestDTO dto) {
        logger.info(">>> [MS-AVALIACOES] Processando correção da tentativa: " + dto.getTentativaId());

        Tentativa tentativa = tentativaRepository.findById(dto.getTentativaId())
                .orElseThrow(() -> new RuntimeException("Tentativa não encontrada"));

        double notaFinalCalculada = 0.0;

        // 1. Itera sobre cada item corrigido pelo professor
        for (CorrecaoRequestDTO.ItemCorrecaoDTO item : dto.getItensCorrecao()) {
            
            // Atualiza a Resposta correspondente com a nota
            Resposta resposta = respostaRepository.findById(item.getRespostaId())
                    .orElseThrow(() -> new RuntimeException("Resposta não encontrada: " + item.getRespostaId()));
            
            resposta.setPontuacao(item.getNotaAtribuida());
            respostaRepository.save(resposta);
            
            // Somatória da nota
            notaFinalCalculada += item.getNotaAtribuida();

            // Opcional: Salvar o feedback na tabela de Correcao (histórico)
            if (item.getComentarios() != null && !item.getComentarios().isEmpty()) {
                Correcao correcaoLog = new Correcao();
                correcaoLog.setTentativa(tentativa);
                correcaoLog.setInstrutorId(1L); // TODO: Pegar ID do instrutor logado do token
                correcaoLog.setFeedback(item.getComentarios());
                correcaoLog.setNotaParcial(item.getNotaAtribuida());
                correcaoLog.setStatus(StatusCorrecao.CORRIGIDA);
                correcaoRepository.save(correcaoLog);
            }
        }

        // 2. Atualiza a Tentativa com a nota final e status
        tentativa.setNotaObtida(notaFinalCalculada);
        tentativa.setStatus(StatusTentativa.CORRIGIDA);
        tentativaRepository.save(tentativa);

        logger.info(">>> [MS-AVALIACOES] Tentativa " + tentativa.getId() + " corrigida. Nota Final: " + notaFinalCalculada);
    }

    // ========== OUTROS MÉTODOS JÁ EXISTENTES (Mantenha-os) ==========

    @Transactional
    public CorrecaoDTO corrigirQuestao(Long tentativaId, Long instrutorId, String feedback, Double nota) {
        Tentativa tentativa = tentativaRepository.findById(tentativaId)
                .orElseThrow(() -> new RuntimeException("Tentativa não encontrada"));

        Correcao correcao = new Correcao();
        correcao.setTentativa(tentativa);
        correcao.setInstrutorId(instrutorId);
        correcao.setFeedback(feedback);
        correcao.setNotaParcial(nota);
        correcao.setStatus(StatusCorrecao.CORRIGIDA);

        Correcao correcaoSalva = correcaoRepository.save(correcao);
        return converterParaDTO(correcaoSalva);
    }

    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarPorInstrutor(Long instrutorId) {
        return correcaoRepository.findByInstrutorId(instrutorId)
                .stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarTodas() {
        return correcaoRepository.findAll()
                .stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarPorTentativa(Long tentativaId) {
        return correcaoRepository.findByTentativaId(tentativaId)
                .stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    private CorrecaoDTO converterParaDTO(Correcao correcao) {
        CorrecaoDTO dto = new CorrecaoDTO();
        dto.setId(correcao.getId());
        dto.setTentativaId(correcao.getTentativa().getId());
        dto.setInstrutorId(correcao.getInstrutorId());
        dto.setFeedback(correcao.getFeedback());
        dto.setNotaParcial(correcao.getNotaParcial());
        dto.setStatus(correcao.getStatus().toString());
        dto.setDataCriacao(correcao.getDataCriacao());
        return dto;
    }
}