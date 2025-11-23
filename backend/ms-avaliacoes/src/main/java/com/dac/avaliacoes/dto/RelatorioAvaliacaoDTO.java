package com.dac.avaliacoes.dto;

public class RelatorioAvaliacaoDTO {
    private String avaliacaoCodigo;
    private String avaliacaoTitulo;
    private String cursoTitulo;
    private Integer totalRealizacoes;
    private Integer totalAprovados;
    private Integer totalReprovados;
    private Double mediaNotas;
    private Double taxaAprovacao;

    // Construtor vazio
    public RelatorioAvaliacaoDTO() {}

    // Construtor completo
    public RelatorioAvaliacaoDTO(String avaliacaoCodigo, String avaliacaoTitulo, String cursoTitulo,
                                 Integer totalRealizacoes, Integer totalAprovados, Integer totalReprovados,
                                 Double mediaNotas, Double taxaAprovacao) {
        this.avaliacaoCodigo = avaliacaoCodigo;
        this.avaliacaoTitulo = avaliacaoTitulo;
        this.cursoTitulo = cursoTitulo;
        this.totalRealizacoes = totalRealizacoes;
        this.totalAprovados = totalAprovados;
        this.totalReprovados = totalReprovados;
        this.mediaNotas = mediaNotas;
        this.taxaAprovacao = taxaAprovacao;
    }

    // Getters e Setters
    public String getAvaliacaoCodigo() { return avaliacaoCodigo; }
    public void setAvaliacaoCodigo(String avaliacaoCodigo) { this.avaliacaoCodigo = avaliacaoCodigo; }

    public String getAvaliacaoTitulo() { return avaliacaoTitulo; }
    public void setAvaliacaoTitulo(String avaliacaoTitulo) { this.avaliacaoTitulo = avaliacaoTitulo; }

    public String getCursoTitulo() { return cursoTitulo; }
    public void setCursoTitulo(String cursoTitulo) { this.cursoTitulo = cursoTitulo; }

    public Integer getTotalRealizacoes() { return totalRealizacoes; }
    public void setTotalRealizacoes(Integer totalRealizacoes) { this.totalRealizacoes = totalRealizacoes; }

    public Integer getTotalAprovados() { return totalAprovados; }
    public void setTotalAprovados(Integer totalAprovados) { this.totalAprovados = totalAprovados; }

    public Integer getTotalReprovados() { return totalReprovados; }
    public void setTotalReprovados(Integer totalReprovados) { this.totalReprovados = totalReprovados; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public Double getTaxaAprovacao() { return taxaAprovacao; }
    public void setTaxaAprovacao(Double taxaAprovacao) { this.taxaAprovacao = taxaAprovacao; }
}
