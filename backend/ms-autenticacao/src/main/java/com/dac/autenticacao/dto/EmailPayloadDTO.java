package com.dac.autenticacao.dto;

public class EmailPayloadDTO {
    private Long usuarioId;
    private String emailDestino;
    private String assunto;
    private String corpo;

    public EmailPayloadDTO(Long usuarioId, String emailDestino, String assunto, String corpo) {
        this.usuarioId = usuarioId;
        this.emailDestino = emailDestino;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    // Getters e Setters necessários para o Jackson converter para JSON
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getEmailDestino() { return emailDestino; }
    public void setEmailDestino(String emailDestino) { this.emailDestino = emailDestino; }
    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
    public String getCorpo() { return corpo; }
    public void setCorpo(String corpo) { this.corpo = corpo; }
}