package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class AvaliacaoDTO {
    private Long id;
    private String codigo;
    private String titulo;
    private String descricao;
    private Long cursoId;
    private Integer tempoLimiteMinutos;
    private Integer tentativasPermitidas;
    private Double notaMinima;
    private Boolean ativo;
    private LocalDateTime dataCriacao;

    private boolean temTentativas;

    // Construtor vazio
    public AvaliacaoDTO() {}

    // Construtor completo (atualizado)
    public AvaliacaoDTO(Long id, String codigo, String titulo, String descricao, Long cursoId,
                        Integer tempoLimiteMinutos, Integer tentativasPermitidas, Double notaMinima,
                        Boolean ativo, LocalDateTime dataCriacao, boolean temTentativas) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.cursoId = cursoId;
        this.tempoLimiteMinutos = tempoLimiteMinutos;
        this.tentativasPermitidas = tentativasPermitidas;
        this.notaMinima = notaMinima;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.temTentativas = temTentativas;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Integer getTempoLimiteMinutos() { return tempoLimiteMinutos; }
    public void setTempoLimiteMinutos(Integer tempoLimiteMinutos) { this.tempoLimiteMinutos = tempoLimiteMinutos; }

    public Integer getTentativasPermitidas() { return tentativasPermitidas; }
    public void setTentativasPermitidas(Integer tentativasPermitidas) { this.tentativasPermitidas = tentativasPermitidas; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public boolean isTemTentativas() { return temTentativas; }
    public void setTemTentativas(boolean temTentativas) { this.temTentativas = temTentativas; }
}