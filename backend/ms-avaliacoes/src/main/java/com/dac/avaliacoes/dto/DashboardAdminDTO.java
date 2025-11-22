package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Map<String, Object>> topFuncionarios; // Top 10 por XP
    private List<Map<String, Object>> topCursos; // Mais realizados
    private List<Map<String, Object>> topAvaliacoes; // Mais realizadas

    // Estatísticas por Departamento
    private List<EstatisticaDepartamentoDTO> estatisticasPorDepartamento;

    // Tendências (últimos 30 dias)
    private Map<String, Integer> tentativasPorDia;
    private Map<String, Double> mediaNotasPorDia;
}
