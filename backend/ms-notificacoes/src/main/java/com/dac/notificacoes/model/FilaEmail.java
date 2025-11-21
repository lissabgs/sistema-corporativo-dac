package com.dac.notificacoes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "filas_email")
public class FilaEmail {

    @Id
    private String id;

    private String destinatario;
    private String assunto;
    private String corpo;
    private LocalDateTime dataEnvio;
    private String status;

    public FilaEmail() {}

    public FilaEmail(String destinatario, String assunto, String corpo, String status) {
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.corpo = corpo;
        this.status = status;
        this.dataEnvio = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
    public String getCorpo() { return corpo; }
    public void setCorpo(String corpo) { this.corpo = corpo; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}