package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RelatorioGeralDTO {

    private LocalDateTime dataGeracao;
    private String periodo;

    // Resumo Executivo
    private Integer totalAvaliacoesRealizadas;
    private Integer totalFuncionariosAtivos;
    private Double mediaNotasGeral;
    private Double taxaAprovacao;

    // Detalhamento por Avaliação
    private List<RelatorioAvaliacaoDTO> detalhePorAvaliacao;

    // Detalhamento por Funcionário
    private List<RelatorioFuncionarioDTO> detalhePorFuncionario;

    // Construtor vazio
    public RelatorioGeralDTO() {}

    // Getters e Setters
    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Integer getTotalAvaliacoesRealizadas() { return totalAvaliacoesRealizadas; }
    public void setTotalAvaliacoesRealizadas(Integer totalAvaliacoesRealizadas) {
        this.totalAvaliacoesRealizadas = totalAvaliacoesRealizadas;
    }

    public Integer getTotalFuncionariosAtivos() { return totalFuncionariosAtivos; }
    public void setTotalFuncionariosAtivos(Integer totalFuncionariosAtivos) {
        this.totalFuncionariosAtivos = totalFuncionariosAtivos;
    }

    public Double getMediaNotasGeral() { return mediaNotasGeral; }
    public void setMediaNotasGeral(Double mediaNotasGeral) { this.mediaNotasGeral = mediaNotasGeral; }

    public Double getTaxaAprovacao() { return taxaAprovacao; }
    public void setTaxaAprovacao(Double taxaAprovacao) { this.taxaAprovacao = taxaAprovacao; }

    public List<RelatorioAvaliacaoDTO> getDetalhePorAvaliacao() { return detalhePorAvaliacao; }
    public void setDetalhePorAvaliacao(List<RelatorioAvaliacaoDTO> detalhePorAvaliacao) {
        this.detalhePorAvaliacao = detalhePorAvaliacao;
    }

    public List<RelatorioFuncionarioDTO> getDetalhePorFuncionario() { return detalhePorFuncionario; }
    public void setDetalhePorFuncionario(List<RelatorioFuncionarioDTO> detalhePorFuncionario) {
        this.detalhePorFuncionario = detalhePorFuncionario;
    }
}