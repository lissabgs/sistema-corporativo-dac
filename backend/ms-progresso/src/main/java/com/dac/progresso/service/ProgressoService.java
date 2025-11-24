package com.dac.progresso.service;

import com.dac.progresso.config.RabbitMQConfig;
import com.dac.progresso.dto.ConcluirAulaRequestDTO;
import com.dac.progresso.model.Progresso;
import com.dac.progresso.model.StatusProgresso;
import com.dac.progresso.repository.ProgressoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // ========== MATRICULAR ALUNO ==========
    @Transactional
    public Progresso matricularAluno(Long funcionarioId, String cursoId) {
        Optional<Progresso> existente = progressoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);

        if (existente.isPresent()) {
            Progresso p = existente.get();
            if (p.getStatus() == StatusProgresso.CANCELADO) {
                p.setStatus(StatusProgresso.INSCRITO);
                return progressoRepository.save(p);
            }
            return p;
        }

        Progresso novoProgresso = new Progresso(funcionarioId, cursoId);
        novoProgresso.setStatus(StatusProgresso.INSCRITO);
        novoProgresso.setDataInicio(LocalDateTime.now());

        return progressoRepository.save(novoProgresso);
    }

    // ========== MÉTODOS CORRIGIDOS (Long -> String) ==========

    // Alterado de Long para String
    public Progresso iniciarCurso(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (progresso.getStatus() == StatusProgresso.INSCRITO ||
                progresso.getStatus() == StatusProgresso.PAUSADO ||
                progresso.getStatus() == StatusProgresso.CANCELADO) {

            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
            return progressoRepository.save(progresso);
        }
        return progresso;
    }

    // Alterado de Long para String
    public Progresso pausarCurso(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if (progresso.getStatus() == StatusProgresso.EM_ANDAMENTO) {
            progresso.setStatus(StatusProgresso.PAUSADO);
            return progressoRepository.save(progresso);
        }
        return progresso;
    }

    // Alterado de Long para String
    public Progresso cancelarInscricao(String progressoId) {
        Progresso progresso = progressoRepository.findById(progressoId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        progresso.setStatus(StatusProgresso.CANCELADO);
        return progressoRepository.save(progresso);
    }

    // =========================================================

    public List<Progresso> listarInscricoesDoAluno(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId);
    }

    public List<String> listarCodigosMatriculados(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId)
                .stream()
                .filter(p -> p.getStatus() != StatusProgresso.CANCELADO)
                .map(Progresso::getCursoId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void concluirAula(ConcluirAulaRequestDTO dto) {
        Progresso progresso = progressoRepository
                .findByFuncionarioIdAndCursoId(dto.getFuncionarioId(), dto.getCursoId())
                .orElse(new Progresso(dto.getFuncionarioId(), dto.getCursoId()));

        if (progresso.getStatus() == StatusProgresso.INSCRITO) {
            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
        }

        boolean aulaNova = progresso.adicionarAula(dto.getAulaId());

        if (progresso.getStatus() == null) {
            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
        }

        progressoRepository.save(progresso);

        if (aulaNova) {
            XpMessageDTO message = new XpMessageDTO(dto.getFuncionarioId(), dto.getXpGanho());
            rabbitTemplate.convertAndSend(RabbitMQConfig.XP_QUEUE_NAME, message);
        }
    }

    public static class XpMessageDTO {
        public Long funcionarioId;
        public int xp;

        public XpMessageDTO(Long funcionarioId, int xp) {
            this.funcionarioId = funcionarioId;
            this.xp = xp;
        }
    }
}