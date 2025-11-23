package com.dac.avaliacoes.dto;

public class EstatisticaDepartamentoDTO {
    private String departamentoNome;
    private Integer totalFuncionarios;
    private Integer totalTentativas;
    private Double mediaNotas;
    private Double taxaAprovacao;
    private Integer totalXp;

    // Construtor vazio
    public EstatisticaDepartamentoDTO() {}

    // Construtor completo
    public EstatisticaDepartamentoDTO(String departamentoNome, Integer totalFuncionarios,
                                      Integer totalTentativas, Double mediaNotas,
                                      Double taxaAprovacao, Integer totalXp) {
        this.departamentoNome = departamentoNome;
        this.totalFuncionarios = totalFuncionarios;
        this.totalTentativas = totalTentativas;
        this.mediaNotas = mediaNotas;
        this.taxaAprovacao = taxaAprovacao;
        this.totalXp = totalXp;
    }

    // Getters e Setters
    public String getDepartamentoNome() { return departamentoNome; }
    public void setDepartamentoNome(String departamentoNome) { this.departamentoNome = departamentoNome; }

    public Integer getTotalFuncionarios() { return totalFuncionarios; }
    public void setTotalFuncionarios(Integer totalFuncionarios) { this.totalFuncionarios = totalFuncionarios; }

    public Integer getTotalTentativas() { return totalTentativas; }
    public void setTotalTentativas(Integer totalTentativas) { this.totalTentativas = totalTentativas; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public Double getTaxaAprovacao() { return taxaAprovacao; }
    public void setTaxaAprovacao(Double taxaAprovacao) { this.taxaAprovacao = taxaAprovacao; }

    public Integer getTotalXp() { return totalXp; }
    public void setTotalXp(Integer totalXp) { this.totalXp = totalXp; }
}
