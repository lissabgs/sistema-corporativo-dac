package com.dac.cursos.dto;

import java.util.List;

public class CursoRequestDTO {

    private String codigo;
    private String titulo;
    private String descricao;
    private String categoriaId;
    private Long instrutorId;
    private String duracaoEstimada;
    private int xpOferecido;
    private String nivelDificuldade;

    // Novo campo de Status (Substitui o boolean ativo)
    private String status;

    private List<String> preRequisitos;

    // Novo campo: Lista de Módulos (que contêm as aulas)
    private List<ModuloDTO> modulos;

    // --- Getters e Setters ---
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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getPreRequisitos() { return preRequisitos; }
    public void setPreRequisitos(List<String> preRequisitos) { this.preRequisitos = preRequisitos; }

    public List<ModuloDTO> getModulos() { return modulos; }
    public void setModulos(List<ModuloDTO> modulos) { this.modulos = modulos; }
}