package com.dac.avaliacoes.dto;

public class CursoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    // Adicione outros campos se necessário

    // Construtor vazio
    public CursoDTO() {}

    // Construtor completo
    public CursoDTO(Long id, String titulo, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}