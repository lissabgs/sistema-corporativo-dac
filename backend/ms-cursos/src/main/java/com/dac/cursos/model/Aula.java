package com.dac.cursos.model;

public class Aula {

    private String titulo;
    private String descricao;
    private String urlConteudo; // Baseado no seu print "Conteúdo/URL"
    private int ordem; // Para saber qual aula vem primeiro (Aula 1, Aula 2...)

    // Construtores
    public Aula() {}

    public Aula(String titulo, String descricao, String urlConteudo, int ordem) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.urlConteudo = urlConteudo;
        this.ordem = ordem;
    }

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