package com.dac.progresso.service;

import com.dac.progresso.config.RabbitMQConfig;
import com.dac.progresso.dto.ConcluirAulaRequestDTO;
import com.dac.progresso.model.Progresso;
import com.dac.progresso.model.StatusProgresso; // Importe o Enum
import com.dac.progresso.repository.ProgressoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime; // Importe para data
import java.util.Optional;
import java.util.stream.Collectors; // <--- IMPORT ADICIONADO

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // ========== MATRICULAR ALUNO (NOVO) ==========
    @Transactional
    public Progresso matricularAluno(Long funcionarioId, String cursoId) {
        // 1. Verifica se já existe matrícula para este aluno neste curso
        Optional<Progresso> existente = progressoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);

        if (existente.isPresent()) {
            // Se já existe, apenas retorna o existente (idempotência)
            // Poderia lançar erro se quisesse ser mais restritivo
            return existente.get();
        }

        // 2. Cria novo registro de progresso
        Progresso novoProgresso = new Progresso(funcionarioId, cursoId);
        novoProgresso.setStatus(StatusProgresso.INSCRITO);
        novoProgresso.setDataInicio(LocalDateTime.now());

        return progressoRepository.save(novoProgresso);
    }

    // ========== CONCLUIR AULA (EXISTENTE) ==========
    @Transactional
    public void concluirAula(ConcluirAulaRequestDTO dto) {

        Progresso progresso = progressoRepository
                .findByFuncionarioIdAndCursoId(dto.getFuncionarioId(), dto.getCursoId())
                .orElse(new Progresso(dto.getFuncionarioId(), dto.getCursoId()));

        if (progresso.getStatus() == StatusProgresso.INSCRITO) {
            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
        }

        boolean aulaNova = progresso.adicionarAula(dto.getAulaId());

        // Atualiza status se necessário (ex: verificar se acabou tudo - lógica futura)
        if (progresso.getStatus() == null) {
            progresso.setStatus(StatusProgresso.EM_ANDAMENTO);
        }

        progressoRepository.save(progresso);

        // Envia XP apenas se for a primeira vez que conclui essa aula
        if (aulaNova) {
            XpMessageDTO message = new XpMessageDTO(dto.getFuncionarioId(), dto.getXpGanho());
            rabbitTemplate.convertAndSend(RabbitMQConfig.XP_QUEUE_NAME, message);
        }
    }

    public List<Progresso> listarInscricoesDoAluno(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId);
    }

    public List<String> listarCodigosMatriculados(Long funcionarioId) {
        return progressoRepository.findByFuncionarioId(funcionarioId)
                .stream()
                .map(Progresso::getCursoId)
                .collect(Collectors.toList());
    }

    // DTO interno para a mensagem RabbitMQ
    public static class XpMessageDTO {
        public Long funcionarioId;
        public int xp;

        public XpMessageDTO(Long funcionarioId, int xp) {
            this.funcionarioId = funcionarioId;
            this.xp = xp;
        }
    }
}