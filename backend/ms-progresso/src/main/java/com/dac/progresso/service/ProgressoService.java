package com.dac.progresso.service;

import com.dac.progresso.config.RabbitMQConfig;
import com.dac.progresso.dto.ConcluirAulaRequestDTO;
import com.dac.progresso.model.Progresso;
import com.dac.progresso.repository.ProgressoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORT QUE FALTAVA

@Service
public class ProgressoService {

    @Autowired
    private ProgressoRepository progressoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional // Esta anotação agora vai funcionar
    public void concluirAula(ConcluirAulaRequestDTO dto) {

        Progresso progresso = progressoRepository
                .findByFuncionarioIdAndCursoId(dto.getFuncionarioId(), dto.getCursoId())
                .orElse(new Progresso(dto.getFuncionarioId(), dto.getCursoId()));

        boolean aulaNova = progresso.adicionarAula(dto.getAulaId());

        progressoRepository.save(progresso);

        if (aulaNova) {
            XpMessageDTO message = new XpMessageDTO(dto.getFuncionarioId(), dto.getXpGanho());
            rabbitTemplate.convertAndSend(RabbitMQConfig.XP_QUEUE_NAME, message);
        }
    }

    // DTO interno para a mensagem
    public static class XpMessageDTO {
        public Long funcionarioId;
        public int xp;

        public XpMessageDTO(Long funcionarioId, int xp) {
            this.funcionarioId = funcionarioId;
            this.xp = xp;
        }
    }
}