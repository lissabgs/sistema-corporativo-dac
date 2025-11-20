package com.dac.avaliacoes.service;

import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TentativaService {

    private static final Logger logger = LoggerFactory.getLogger(TentativaService.class);

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // ========== INICIAR TENTATIVA ==========
    @Transactional
    public Tentativa iniciarTentativa(Long funcionarioId, Long avaliacaoId) {
        logger.info(">>> [MS-AVALIACOES] Iniciando tentativa para funcionário: " + funcionarioId);

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Tentativa tentativa = new Tentativa();
        tentativa.setFuncionarioId(funcionarioId);
        tentativa.setAvaliacao(avaliacao);
        tentativa.setDataInicio(LocalDateTime.now());
        tentativa.setStatus(StatusTentativa.EM_PROGRESSO);
        tentativa.setNumeroTentativa(1);

        Tentativa tentativaSalva = tentativaRepository.save(tentativa);
        logger.info(">>> [MS-AVALIACOES] Tentativa iniciada com ID: " + tentativaSalva.getId());

        return tentativaSalva;
    }

    // ========== FINALIZAR TENTATIVA ==========
    @Transactional
    public void finalizarTentativa(Long tentativaId, Double notaFinal) {
        logger.info(">>> [MS-AVALIACOES] Finalizando tentativa: " + tentativaId);

        Tentativa tentativa = tentativaRepository.findById(tentativaId)
                .orElseThrow(() -> new RuntimeException("Tentativa não encontrada"));

        tentativa.setDataFim(LocalDateTime.now());
        tentativa.setNotaObtida(notaFinal);
        tentativa.setStatus(StatusTentativa.CONCLUIDA);

        tentativaRepository.save(tentativa);
        logger.info(">>> [MS-AVALIACOES] Nota obtida: " + notaFinal);
    }

    // ========== LISTAR POR AVALIAÇÃO ==========
    @Transactional(readOnly = true)
    public List<Tentativa> listarPorAvaliacao(Long avaliacaoId) {
        return tentativaRepository.findByAvaliacaoId(avaliacaoId);
    }

    // ========== LISTAR POR FUNCIONÁRIO ==========
    @Transactional(readOnly = true)
    public List<Tentativa> listarPorFuncionario(Long funcionarioId) {
        return tentativaRepository.findByFuncionarioId(funcionarioId);
    }
}