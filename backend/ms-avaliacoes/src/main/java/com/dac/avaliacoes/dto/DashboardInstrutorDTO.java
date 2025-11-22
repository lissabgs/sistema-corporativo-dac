package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardInstrutorDTO {

    // Estatísticas Gerais
    private Long totalAvaliacoesCriadas;
    private Long totalTentativasRealizadas;
    private Long tentativasPendentesCorrecao;
    private Double mediaNotasGeral;

    // Avaliações Recentes
    private List<AvaliacaoResumoDTO> avaliacoesRecentes;

    // Tentativas Pendentes de Correção
    private List<TentativaPendenteDTO> tentativasPendentes;

    // Estatísticas por Avaliação
    private List<EstatisticaAvaliacaoDTO> estatisticasPorAvaliacao;
}
