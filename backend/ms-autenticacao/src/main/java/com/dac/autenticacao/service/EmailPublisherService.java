package com.dac.autenticacao.service;

import com.dac.autenticacao.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailPublisherService {

    private static final Logger logger = LoggerFactory.getLogger(EmailPublisherService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarEmailBoasVindas(Long usuarioId, String emailDestino, String nome, String senha) {
        EmailPayload payload = new EmailPayload();
        payload.setUsuarioId(usuarioId);
        payload.setEmailDestino(emailDestino);
        payload.setAssunto("Bem-vindo ao Sistema E-Learning");
        payload.setCorpo(String.format(
                "Olá %s,\n\n" +
                        "Seu cadastro foi realizado com sucesso!\n\n" +
                        "Seus dados de acesso:\n" +
                        "Email: %s\n" +
                        "Senha temporária: %s\n\n" +
                        "Por favor, altere sua senha no primeiro acesso.\n\n" +
                        "Atenciosamente,\nEquipe E-Learning",
                nome, emailDestino, senha
        ));

        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, payload);
        logger.info(">>> [MS-AUTENTICACAO] Email de boas-vindas enviado para a fila: " + emailDestino);
    }

    // Classe interna para o payload
    public static class EmailPayload {
        private Long usuarioId;
        private String emailDestino;
        private String assunto;
        private String corpo;

        // Getters e Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        public String getEmailDestino() { return emailDestino; }
        public void setEmailDestino(String emailDestino) { this.emailDestino = emailDestino; }
        public String getAssunto() { return assunto; }
        public void setAssunto(String assunto) { this.assunto = assunto; }
        public String getCorpo() { return corpo; }
        public void setCorpo(String corpo) { this.corpo = corpo; }
    }
}