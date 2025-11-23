package com.dac.avaliacoes.dto;

import java.util.List;

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
    private List<RelatorioGeralDTO.EstatisticaAvaliacaoDTO> estatisticasPorAvaliacao;

    // Construtor vazio
    public DashboardInstrutorDTO() {}

    // Construtor completo
    public DashboardInstrutorDTO(Long totalAvaliacoesCriadas, Long totalTentativasRealizadas,
                                 Long tentativasPendentesCorrecao, Double mediaNotasGeral,
                                 List<AvaliacaoResumoDTO> avaliacoesRecentes,
                                 List<TentativaPendenteDTO> tentativasPendentes,
                                 List<RelatorioGeralDTO.EstatisticaAvaliacaoDTO> estatisticasPorAvaliacao) {
        this.totalAvaliacoesCriadas = totalAvaliacoesCriadas;
        this.totalTentativasRealizadas = totalTentativasRealizadas;
        this.tentativasPendentesCorrecao = tentativasPendentesCorrecao;
        this.mediaNotasGeral = mediaNotasGeral;
        this.avaliacoesRecentes = avaliacoesRecentes;
        this.tentativasPendentes = tentativasPendentes;
        this.estatisticasPorAvaliacao = estatisticasPorAvaliacao;
    }

    // Getters e Setters
    public Long getTotalAvaliacoesCriadas() { return totalAvaliacoesCriadas; }
    public void setTotalAvaliacoesCriadas(Long totalAvaliacoesCriadas) { this.totalAvaliacoesCriadas = totalAvaliacoesCriadas; }

    public Long getTotalTentativasRealizadas() { return totalTentativasRealizadas; }
    public void setTotalTentativasRealizadas(Long totalTentativasRealizadas) { this.totalTentativasRealizadas = totalTentativasRealizadas; }

    public Long getTentativasPendentesCorrecao() { return tentativasPendentesCorrecao; }
    public void setTentativasPendentesCorrecao(Long tentativasPendentesCorrecao) { this.tentativasPendentesCorrecao = tentativasPendentesCorrecao; }

    public Double getMediaNotasGeral() { return mediaNotasGeral; }
    public void setMediaNotasGeral(Double mediaNotasGeral) { this.mediaNotasGeral = mediaNotasGeral; }

    public List<AvaliacaoResumoDTO> getAvaliacoesRecentes() { return avaliacoesRecentes; }
    public void setAvaliacoesRecentes(List<AvaliacaoResumoDTO> avaliacoesRecentes) { this.avaliacoesRecentes = avaliacoesRecentes; }

    public List<TentativaPendenteDTO> getTentativasPendentes() { return tentativasPendentes; }
    public void setTentativasPendentes(List<TentativaPendenteDTO> tentativasPendentes) { this.tentativasPendentes = tentativasPendentes; }

    public List<RelatorioGeralDTO.EstatisticaAvaliacaoDTO> getEstatisticasPorAvaliacao() { return estatisticasPorAvaliacao; }
    public void setEstatisticasPorAvaliacao(List<RelatorioGeralDTO.EstatisticaAvaliacaoDTO> estatisticasPorAvaliacao) { this.estatisticasPorAvaliacao = estatisticasPorAvaliacao; }
}
