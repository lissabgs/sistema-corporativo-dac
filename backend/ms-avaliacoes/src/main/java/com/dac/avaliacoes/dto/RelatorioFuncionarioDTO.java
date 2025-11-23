package com.dac.avaliacoes.dto;

public class RelatorioFuncionarioDTO {
    private String funcionarioNome;
    private String departamento;
    private Integer totalAvaliacoes;
    private Integer totalAprovacoes;
    private Double mediaNotas;
    private Integer xpTotal;
    private String nivel;

    // Construtor vazio
    public RelatorioFuncionarioDTO() {}

    // Construtor completo
    public RelatorioFuncionarioDTO(String funcionarioNome, String departamento, Integer totalAvaliacoes,
                                   Integer totalAprovacoes, Double mediaNotas, Integer xpTotal, String nivel) {
        this.funcionarioNome = funcionarioNome;
        this.departamento = departamento;
        this.totalAvaliacoes = totalAvaliacoes;
        this.totalAprovacoes = totalAprovacoes;
        this.mediaNotas = mediaNotas;
        this.xpTotal = xpTotal;
        this.nivel = nivel;
    }

    // Getters e Setters
    public String getFuncionarioNome() { return funcionarioNome; }
    public void setFuncionarioNome(String funcionarioNome) { this.funcionarioNome = funcionarioNome; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Integer getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(Integer totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }

    public Integer getTotalAprovacoes() { return totalAprovacoes; }
    public void setTotalAprovacoes(Integer totalAprovacoes) { this.totalAprovacoes = totalAprovacoes; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public Integer getXpTotal() { return xpTotal; }
    public void setXpTotal(Integer xpTotal) { this.xpTotal = xpTotal; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}