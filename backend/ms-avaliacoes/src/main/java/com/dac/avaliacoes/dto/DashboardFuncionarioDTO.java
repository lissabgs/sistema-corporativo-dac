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
public class DashboardFuncionarioDTO {

    // Dados do Funcionário
    private String nome;
    private String email;
    private String departamento;

    // Gamificação
    private Integer xpTotal;
    private String nivel;
    private Integer xpProximoNivel;
    private Double progressoNivel; // percentual
    private List<String> badges;

    // Estatísticas de Avaliações
    private Integer totalAvaliacoesRealizadas;
    private Integer avaliacoesAprovadas;
    private Integer avaliacoesReprovadas;
    private Double mediaNotasGeral;
    private Double taxaAprovacao;

    // Cursos em Progresso
    private List<CursoProgressoDTO> cursosEmProgresso;

    // Avaliações Disponíveis
    private List<AvaliacaoDisponivelDTO> avaliacoesDisponiveis;

    // Histórico Recente
    private List<TentativaHistoricoDTO> historicoRecente;
}
