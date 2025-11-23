package com.dac.avaliacoes.dto;

import java.util.List;

public class CriarAvaliacaoRequestDTO {
    private String codigo;
    private String titulo;
    private String descricao;
    private Long cursoId;
    private Integer tempoLimiteMinutos;
    private Integer tentativasPermitidas;
    private Double notaMinima;
    private List<QuestaoDTO> questoes;

    // Construtor vazio
    public CriarAvaliacaoRequestDTO() {}

    // Construtor completo
    public CriarAvaliacaoRequestDTO(String codigo, String titulo, String descricao, Long cursoId,
                                    Integer tempoLimiteMinutos, Integer tentativasPermitidas,
                                    Double notaMinima, List<QuestaoDTO> questoes) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.cursoId = cursoId;
        this.tempoLimiteMinutos = tempoLimiteMinutos;
        this.tentativasPermitidas = tentativasPermitidas;
        this.notaMinima = notaMinima;
        this.questoes = questoes;
    }

    // Getters e Setters
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

    public List<QuestaoDTO> getQuestoes() { return questoes; }
    public void setQuestoes(List<QuestaoDTO> questoes) { this.questoes = questoes; }
}
