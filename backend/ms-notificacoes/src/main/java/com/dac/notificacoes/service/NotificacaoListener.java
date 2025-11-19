package com.dac.notificacoes.service;

import com.dac.notificacoes.config.RabbitMQConfig;
import com.dac.notificacoes.model.FilaEmail;
import com.dac.notificacoes.model.Notificacao;
import com.dac.notificacoes.repository.FilaEmailRepository;
import com.dac.notificacoes.repository.NotificacaoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private FilaEmailRepository filaEmailRepository;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receberMensagem(EmailPayload payload) {
        System.out.println(">>> Nova mensagem recebida na fila: " + payload.getAssunto());

        String statusEnvio;

        // 1. Tenta enviar o e-mail
        try {
            emailService.enviar(payload.getEmailDestino(), payload.getAssunto(), payload.getCorpo());
            statusEnvio = "ENVIADO";
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            statusEnvio = "ERRO: " + e.getMessage();
        }

        // 2. Salva na tabela 'Filas_Email'
        FilaEmail logEmail = new FilaEmail(
            payload.getEmailDestino(),
            payload.getAssunto(),
            payload.getCorpo(),
            statusEnvio
        );
        filaEmailRepository.save(logEmail);
        System.out.println(">>> Log de e-mail salvo com status: " + statusEnvio);

        // 3. Salva na tabela 'Notificacoes'
        Notificacao notif = new Notificacao(
            payload.getUsuarioId(),
            payload.getAssunto(),
            payload.getCorpo(),
            "SISTEMA",
            "EMAIL"
        );
        notificacaoRepository.save(notif);
        System.out.println(">>> Notificação do usuário salva.");
    }

    // Classe auxiliar (DTO) sem Lombok
    public static class EmailPayload {
        private Long usuarioId;
        private String emailDestino;
        private String assunto;
        private String corpo;

        public EmailPayload() {}

        // Getters e Setters manuais (Isso resolve o erro de compilação!)
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