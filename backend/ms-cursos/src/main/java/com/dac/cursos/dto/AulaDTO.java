package com.dac.cursos.dto;

// DTO para receber dados da aula
public class AulaDTO {

    private String titulo;
    private String descricao;
    private String urlConteudo;
    private int ordem;

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getUrlConteudo() { return urlConteudo; }
    public void setUrlConteudo(String urlConteudo) { this.urlConteudo = urlConteudo; }
    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
}