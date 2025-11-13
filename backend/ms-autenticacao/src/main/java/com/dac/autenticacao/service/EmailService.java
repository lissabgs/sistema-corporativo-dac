package com.dac.autenticacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarSenhaInicial(String paraEmail, String senha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nao-responda@empresa.com");
        message.setTo(paraEmail);
        message.setSubject("Bem-vindo! Sua senha de acesso.");
        message.setText("Olá,\n\nSeu cadastro foi realizado com sucesso.\n\n" +
                "Sua senha inicial de acesso é: " + senha +
                "\n\nRecomendamos que troque a senha no seu primeiro acesso." +
                "\n\nAtenciosamente,\nEquipe de E-Learning");

        mailSender.send(message);
    }
}