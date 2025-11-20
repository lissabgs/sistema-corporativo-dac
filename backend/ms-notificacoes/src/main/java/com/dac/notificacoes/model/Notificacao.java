package com.dac.notificacoes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;
    
    private Long usuarioId;
    private String titulo;
    private String mensagem;
    private String tipo;
    private String canal;
    private LocalDateTime dataCriacao;
    private boolean lida;
    
    public Notificacao() {}

    public Notificacao(Long usuarioId, String titulo, String mensagem, String tipo, String canal) {
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.canal = canal;
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
    }

    // Getters e Setters manuais
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
}