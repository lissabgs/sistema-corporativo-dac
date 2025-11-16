package com.dac.gamificacao.service;

import com.dac.gamificacao.config.RabbitMQConfig;
import com.dac.gamificacao.model.Pontuacao;
import com.dac.gamificacao.repository.PontuacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class XpListenerService {

    private static final Logger logger = LoggerFactory.getLogger(XpListenerService.class);

    @Autowired
    private PontuacaoRepository pontuacaoRepository;

    /**
     * R08: Ouve a fila de XP e atualiza a pontuação
     */
    @RabbitListener(queues = RabbitMQConfig.XP_QUEUE_NAME) // Ouve a fila
    @Transactional
    public void onAulaConcluida(XpMessageDTO message) {
        logger.info(">>> [MS-GAMIFICACAO] Mensagem de XP recebida para funcionário: " + message.getFuncionarioId());

        // 1. Encontra ou cria o documento de pontuação
        Pontuacao pontuacao = pontuacaoRepository
                .findByFuncionarioId(message.getFuncionarioId())
                .orElse(new Pontuacao(message.getFuncionarioId()));

        // 2. Adiciona o XP
        int novoXp = pontuacao.getXpTotal() + message.getXp();
        pontuacao.setXpTotal(novoXp);

        // 3. Salva no MongoDB
        pontuacaoRepository.save(pontuacao);

        logger.info(">>> [MS-GAMIFICACAO] XP atualizado para: " + novoXp);
    }

    /**
     * DTO interno para a mensagem (deve ser IDÊNTICO ao do ms-progresso)
     * O Spring AMQP usa Jackson para desserializar o JSON da mensagem
     */
    public static class XpMessageDTO {
        private Long funcionarioId;
        private int xp;

        // Construtor vazio é OBRIGATÓRIO para o Jackson
        public XpMessageDTO() {}

        public XpMessageDTO(Long funcionarioId, int xp) {
            this.funcionarioId = funcionarioId;
            this.xp = xp;
        }

        // Getters são necessários para o Jackson
        public Long getFuncionarioId() { return funcionarioId; }
        public int getXp() { return xp; }
    }
}