package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioGeralDTO {

    private LocalDateTime dataGeracao;
    private String periodo; // ex: "Janeiro 2025", "Últimos 30 dias"

    // Resumo Executivo
    private Integer totalAvaliacoesRealizadas;
    private Integer totalFuncionariosAtivos;
    private Double mediaNotasGeral;
    private Double taxaAprovacao;

    // Detalhamento por Avaliação
    private List<RelatorioAvaliacaoDTO> detalhePorAvaliacao;

    // Detalhamento por Funcionário
    private List<RelatorioFuncionarioDTO> detalhePorFuncionario;
}
