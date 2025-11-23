package com.dac.avaliacoes.dto;

public class CursoProgressoDTO {
    private String cursoId;
    private String titulo;
    private String descricao;
    private Integer totalAulas;
    private Integer aulasConcluidas;
    private Double progressoPercentual;
    private Integer xpGanho;
    private Integer xpTotal;

    // Construtor vazio
    public CursoProgressoDTO() {}

    // Construtor completo
    public CursoProgressoDTO(String cursoId, String titulo, String descricao, Integer totalAulas,
                             Integer aulasConcluidas, Double progressoPercentual,
                             Integer xpGanho, Integer xpTotal) {
        this.cursoId = cursoId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.totalAulas = totalAulas;
        this.aulasConcluidas = aulasConcluidas;
        this.progressoPercentual = progressoPercentual;
        this.xpGanho = xpGanho;
        this.xpTotal = xpTotal;
    }

    // Getters e Setters
    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getTotalAulas() { return totalAulas; }
    public void setTotalAulas(Integer totalAulas) { this.totalAulas = totalAulas; }

    public Integer getAulasConcluidas() { return aulasConcluidas; }
    public void setAulasConcluidas(Integer aulasConcluidas) { this.aulasConcluidas = aulasConcluidas; }

    public Double getProgressoPercentual() { return progressoPercentual; }
    public void setProgressoPercentual(Double progressoPercentual) { this.progressoPercentual = progressoPercentual; }

    public Integer getXpGanho() { return xpGanho; }
    public void setXpGanho(Integer xpGanho) { this.xpGanho = xpGanho; }

    public Integer getXpTotal() { return xpTotal; }
    public void setXpTotal(Integer xpTotal) { this.xpTotal = xpTotal; }
}
