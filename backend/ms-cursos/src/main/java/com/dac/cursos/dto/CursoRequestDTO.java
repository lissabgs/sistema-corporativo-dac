package com.dac.cursos.dto;

import java.util.List;

// DTO para receber o JSON completo do novo curso
public class CursoRequestDTO {

    private String codigo;
    private String titulo;
    private String descricao;
    private String categoriaId;
    private Long instrutorId;
    private String duracaoEstimada;
    private int xpOferecido;
    private String nivelDificuldade;
    private boolean ativo;
    private List<String> preRequisitos;
    private List<AulaDTO> aulas; // Recebe a lista de DTOs de aulas

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public Long getInstrutorId() { return instrutorId; }
    public void setInstrutorId(Long instrutorId) { this.instrutorId = instrutorId; }
    public String getDuracaoEstimada() { return duracaoEstimada; }
    public void setDuracaoEstimada(String duracaoEstimada) { this.duracaoEstimada = duracaoEstimada; }
    public int getXpOferecido() { return xpOferecido; }
    public void setXpOferecido(int xpOferecido) { this.xpOferecido = xpOferecido; }
    public String getNivelDificuldade() { return nivelDificuldade; }
    public void setNivelDificuldade(String nivelDificuldade) { this.nivelDificuldade = nivelDificuldade; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<String> getPreRequisitos() { return preRequisitos; }
    public void setPreRequisitos(List<String> preRequisitos) { this.preRequisitos = preRequisitos; }
    public List<AulaDTO> getAulas() { return aulas; }
    public void setAulas(List<AulaDTO> aulas) { this.aulas = aulas; }
}