package com.dac.notificacoes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Pega o remetente configurado no application.yml
    @Value("${spring.mail.username}")
    private String remetente;

    public void enviar(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            
            mailSender.send(message);
            System.out.println(">>> E-mail enviado com sucesso para: " + para);
        } catch (Exception e) {
            // Se der erro (ex: senha errada), mostramos no console mas não travamos o sistema
            System.err.println(">>> Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}