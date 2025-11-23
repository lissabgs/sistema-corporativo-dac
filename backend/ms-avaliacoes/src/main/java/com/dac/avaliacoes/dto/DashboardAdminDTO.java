package com.dac.avaliacoes.dto;

import java.util.List;
import java.util.Map;

public class DashboardAdminDTO {

    // Estatísticas Gerais do Sistema
    private Long totalFuncionarios;
    private Long totalInstrutores;
    private Long totalCursos;
    private Long totalAvaliacoes;
    private Long totalTentativas;

    // Métricas de Desempenho
    private Double mediaNotasGeral;
    private Double taxaAprovacaoGeral;
    private Double taxaConclusaoCursos;

    // Top Performers
    private List<Map<String, Object>> topFuncionarios;
    private List<Map<String, Object>> topCursos;
    private List<Map<String, Object>> topAvaliacoes;

    // Estatísticas por Departamento
    private List<EstatisticaDepartamentoDTO> estatisticasPorDepartamento;

    // Tendências (últimos 30 dias)
    private Map<String, Integer> tentativasPorDia;
    private Map<String, Double> mediaNotasPorDia;

    // Construtor vazio
    public DashboardAdminDTO() {}

    // Getters e Setters
    public Long getTotalFuncionarios() { return totalFuncionarios; }
    public void setTotalFuncionarios(Long totalFuncionarios) { this.totalFuncionarios = totalFuncionarios; }

    public Long getTotalInstrutores() { return totalInstrutores; }
    public void setTotalInstrutores(Long totalInstrutores) { this.totalInstrutores = totalInstrutores; }

    public Long getTotalCursos() { return totalCursos; }
    public void setTotalCursos(Long totalCursos) { this.totalCursos = totalCursos; }

    public Long getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(Long totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }

    public Long getTotalTentativas() { return totalTentativas; }
    public void setTotalTentativas(Long totalTentativas) { this.totalTentativas = totalTentativas; }

    public Double getMediaNotasGeral() { return mediaNotasGeral; }
    public void setMediaNotasGeral(Double mediaNotasGeral) { this.mediaNotasGeral = mediaNotasGeral; }

    public Double getTaxaAprovacaoGeral() { return taxaAprovacaoGeral; }
    public void setTaxaAprovacaoGeral(Double taxaAprovacaoGeral) { this.taxaAprovacaoGeral = taxaAprovacaoGeral; }

    public Double getTaxaConclusaoCursos() { return taxaConclusaoCursos; }
    public void setTaxaConclusaoCursos(Double taxaConclusaoCursos) { this.taxaConclusaoCursos = taxaConclusaoCursos; }

    public List<Map<String, Object>> getTopFuncionarios() { return topFuncionarios; }
    public void setTopFuncionarios(List<Map<String, Object>> topFuncionarios) { this.topFuncionarios = topFuncionarios; }

    public List<Map<String, Object>> getTopCursos() { return topCursos; }
    public void setTopCursos(List<Map<String, Object>> topCursos) { this.topCursos = topCursos; }

    public List<Map<String, Object>> getTopAvaliacoes() { return topAvaliacoes; }
    public void setTopAvaliacoes(List<Map<String, Object>> topAvaliacoes) { this.topAvaliacoes = topAvaliacoes; }

    public List<EstatisticaDepartamentoDTO> getEstatisticasPorDepartamento() { return estatisticasPorDepartamento; }
    public void setEstatisticasPorDepartamento(List<EstatisticaDepartamentoDTO> estatisticasPorDepartamento) {
        this.estatisticasPorDepartamento = estatisticasPorDepartamento;
    }

    public Map<String, Integer> getTentativasPorDia() { return tentativasPorDia; }
    public void setTentativasPorDia(Map<String, Integer> tentativasPorDia) { this.tentativasPorDia = tentativasPorDia; }

    public Map<String, Double> getMediaNotasPorDia() { return mediaNotasPorDia; }
    public void setMediaNotasPorDia(Map<String, Double> mediaNotasPorDia) { this.mediaNotasPorDia = mediaNotasPorDia; }
}
