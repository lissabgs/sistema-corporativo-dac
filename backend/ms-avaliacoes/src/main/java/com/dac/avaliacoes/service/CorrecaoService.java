package com.dac.avaliacoes.service;

import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import com.dac.avaliacoes.dto.CorrecaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CorrecaoService {

    private static final Logger logger = LoggerFactory.getLogger(CorrecaoService.class);

    @Autowired
    private CorrecaoRepository correcaoRepository;

    @Autowired
    private TentativaRepository tentativaRepository;

    // ========== CORRIGIR QUESTÃO ==========
    @Transactional
    public CorrecaoDTO corrigirQuestao(Long tentativaId, Long instrutorId, String feedback, Double nota) {
        logger.info(">>> [MS-AVALIACOES] Corrigindo questão da tentativa: " + tentativaId);

        Tentativa tentativa = tentativaRepository.findById(tentativaId)
                .orElseThrow(() -> new RuntimeException("Tentativa não encontrada"));

        Correcao correcao = new Correcao();
        correcao.setTentativa(tentativa);
        correcao.setInstrutorId(instrutorId);
        correcao.setFeedback(feedback);
        correcao.setNotaParcial(nota);
        correcao.setStatus(StatusCorrecao.CORRIGIDA);

        Correcao correcaoSalva = correcaoRepository.save(correcao);
        logger.info(">>> [MS-AVALIACOES] Correção salva com ID: " + correcaoSalva.getId());

        return converterParaDTO(correcaoSalva);
    }

    // ========== LISTAR POR INSTRUTOR ==========
    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarPorInstrutor(Long instrutorId) {
        return correcaoRepository.findByInstrutorId(instrutorId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ========== LISTAR TODAS ==========
    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarTodas() {
        return correcaoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ========== LISTAR POR TENTATIVA ==========
    @Transactional(readOnly = true)
    public List<CorrecaoDTO> listarPorTentativa(Long tentativaId) {
        return correcaoRepository.findByTentativaId(tentativaId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ========== CONVERTER PARA DTO ==========
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
