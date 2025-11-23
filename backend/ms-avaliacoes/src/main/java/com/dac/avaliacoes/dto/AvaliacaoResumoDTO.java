package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class AvaliacaoResumoDTO {
    private Long id;
    private String codigo;
    private String titulo;
    private Long cursoId;
    private Integer totalTentativas;
    private Double mediaNotas;
    private LocalDateTime dataCriacao;

    // Construtor vazio
    public AvaliacaoResumoDTO() {}

    // Construtor completo
    public AvaliacaoResumoDTO(Long id, String codigo, String titulo, Long cursoId,
                              Integer totalTentativas, Double mediaNotas, LocalDateTime dataCriacao) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.cursoId = cursoId;
        this.totalTentativas = totalTentativas;
        this.mediaNotas = mediaNotas;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Integer getTotalTentativas() { return totalTentativas; }
    public void setTotalTentativas(Integer totalTentativas) { this.totalTentativas = totalTentativas; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
