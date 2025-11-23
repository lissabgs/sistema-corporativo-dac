package com.dac.avaliacoes.dto;

public class EstatisticaAvaliacaoDTO {
    private Long avaliacaoId;
    private String avaliacaoTitulo;
    private Integer totalTentativas;
    private Integer tentativasAprovadas;
    private Integer tentativasReprovadas;
    private Double mediaNotas;
    private Double notaMinima;
    private Double notaMaxima;
    private Double taxaAprovacao;

    // Construtor vazio
    public EstatisticaAvaliacaoDTO() {}

    // Construtor completo
    public EstatisticaAvaliacaoDTO(Long avaliacaoId, String avaliacaoTitulo, Integer totalTentativas,
                                   Integer tentativasAprovadas, Integer tentativasReprovadas,
                                   Double mediaNotas, Double notaMinima, Double notaMaxima, Double taxaAprovacao) {
        this.avaliacaoId = avaliacaoId;
        this.avaliacaoTitulo = avaliacaoTitulo;
        this.totalTentativas = totalTentativas;
        this.tentativasAprovadas = tentativasAprovadas;
        this.tentativasReprovadas = tentativasReprovadas;
        this.mediaNotas = mediaNotas;
        this.notaMinima = notaMinima;
        this.notaMaxima = notaMaxima;
        this.taxaAprovacao = taxaAprovacao;
    }

    // Getters e Setters
    public Long getAvaliacaoId() { return avaliacaoId; }
    public void setAvaliacaoId(Long avaliacaoId) { this.avaliacaoId = avaliacaoId; }

    public String getAvaliacaoTitulo() { return avaliacaoTitulo; }
    public void setAvaliacaoTitulo(String avaliacaoTitulo) { this.avaliacaoTitulo = avaliacaoTitulo; }

    public Integer getTotalTentativas() { return totalTentativas; }
    public void setTotalTentativas(Integer totalTentativas) { this.totalTentativas = totalTentativas; }

    public Integer getTentativasAprovadas() { return tentativasAprovadas; }
    public void setTentativasAprovadas(Integer tentativasAprovadas) { this.tentativasAprovadas = tentativasAprovadas; }

    public Integer getTentativasReprovadas() { return tentativasReprovadas; }
    public void setTentativasReprovadas(Integer tentativasReprovadas) { this.tentativasReprovadas = tentativasReprovadas; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public Double getNotaMaxima() { return notaMaxima; }
    public void setNotaMaxima(Double notaMaxima) { this.notaMaxima = notaMaxima; }

    public Double getTaxaAprovacao() { return taxaAprovacao; }
    public void setTaxaAprovacao(Double taxaAprovacao) { this.taxaAprovacao = taxaAprovacao; }
}
