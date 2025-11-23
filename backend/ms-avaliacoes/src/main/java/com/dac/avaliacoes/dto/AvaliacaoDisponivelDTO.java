package com.dac.avaliacoes.dto;

public class AvaliacaoDisponivelDTO {
    private Long avaliacaoId;
    private String codigo;
    private String titulo;
    private String descricao;
    private Integer tempoLimiteMinutos;
    private Integer tentativasRestantes;
    private Integer tentativasPermitidas;
    private Double notaMinima;
    private String cursoTitulo;

    // Construtor vazio
    public AvaliacaoDisponivelDTO() {}

    // Construtor completo
    public AvaliacaoDisponivelDTO(Long avaliacaoId, String codigo, String titulo, String descricao,
                                  Integer tempoLimiteMinutos, Integer tentativasRestantes,
                                  Integer tentativasPermitidas, Double notaMinima, String cursoTitulo) {
        this.avaliacaoId = avaliacaoId;
        this.codigo = codigo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempoLimiteMinutos = tempoLimiteMinutos;
        this.tentativasRestantes = tentativasRestantes;
        this.tentativasPermitidas = tentativasPermitidas;
        this.notaMinima = notaMinima;
        this.cursoTitulo = cursoTitulo;
    }

    // Getters e Setters
    public Long getAvaliacaoId() { return avaliacaoId; }
    public void setAvaliacaoId(Long avaliacaoId) { this.avaliacaoId = avaliacaoId; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getTempoLimiteMinutos() { return tempoLimiteMinutos; }
    public void setTempoLimiteMinutos(Integer tempoLimiteMinutos) { this.tempoLimiteMinutos = tempoLimiteMinutos; }

    public Integer getTentativasRestantes() { return tentativasRestantes; }
    public void setTentativasRestantes(Integer tentativasRestantes) { this.tentativasRestantes = tentativasRestantes; }

    public Integer getTentativasPermitidas() { return tentativasPermitidas; }
    public void setTentativasPermitidas(Integer tentativasPermitidas) { this.tentativasPermitidas = tentativasPermitidas; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public String getCursoTitulo() { return cursoTitulo; }
    public void setCursoTitulo(String cursoTitulo) { this.cursoTitulo = cursoTitulo; }
}
