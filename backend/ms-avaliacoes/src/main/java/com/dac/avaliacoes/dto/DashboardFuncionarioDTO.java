package com.dac.avaliacoes.dto;

import java.util.List;

public class DashboardFuncionarioDTO {

    // Dados do Funcionário
    private String nome;
    private String email;
    private String departamento;

    // Gamificação
    private Integer xpTotal;
    private String nivel;
    private Integer xpProximoNivel;
    private Double progressoNivel;
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

    // Construtor vazio
    public DashboardFuncionarioDTO() {}

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Integer getXpTotal() { return xpTotal; }
    public void setXpTotal(Integer xpTotal) { this.xpTotal = xpTotal; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getXpProximoNivel() { return xpProximoNivel; }
    public void setXpProximoNivel(Integer xpProximoNivel) { this.xpProximoNivel = xpProximoNivel; }

    public Double getProgressoNivel() { return progressoNivel; }
    public void setProgressoNivel(Double progressoNivel) { this.progressoNivel = progressoNivel; }

    public List<String> getBadges() { return badges; }
    public void setBadges(List<String> badges) { this.badges = badges; }

    public Integer getTotalAvaliacoesRealizadas() { return totalAvaliacoesRealizadas; }
    public void setTotalAvaliacoesRealizadas(Integer totalAvaliacoesRealizadas) {
        this.totalAvaliacoesRealizadas = totalAvaliacoesRealizadas;
    }

    public Integer getAvaliacoesAprovadas() { return avaliacoesAprovadas; }
    public void setAvaliacoesAprovadas(Integer avaliacoesAprovadas) { this.avaliacoesAprovadas = avaliacoesAprovadas; }

    public Integer getAvaliacoesReprovadas() { return avaliacoesReprovadas; }
    public void setAvaliacoesReprovadas(Integer avaliacoesReprovadas) { this.avaliacoesReprovadas = avaliacoesReprovadas; }

    public Double getMediaNotasGeral() { return mediaNotasGeral; }
    public void setMediaNotasGeral(Double mediaNotasGeral) { this.mediaNotasGeral = mediaNotasGeral; }

    public Double getTaxaAprovacao() { return taxaAprovacao; }
    public void setTaxaAprovacao(Double taxaAprovacao) { this.taxaAprovacao = taxaAprovacao; }

    public List<CursoProgressoDTO> getCursosEmProgresso() { return cursosEmProgresso; }
    public void setCursosEmProgresso(List<CursoProgressoDTO> cursosEmProgresso) {
        this.cursosEmProgresso = cursosEmProgresso;
    }

    public List<AvaliacaoDisponivelDTO> getAvaliacoesDisponiveis() { return avaliacoesDisponiveis; }
    public void setAvaliacoesDisponiveis(List<AvaliacaoDisponivelDTO> avaliacoesDisponiveis) {
        this.avaliacoesDisponiveis = avaliacoesDisponiveis;
    }

    public List<TentativaHistoricoDTO> getHistoricoRecente() { return historicoRecente; }
    public void setHistoricoRecente(List<TentativaHistoricoDTO> historicoRecente) {
        this.historicoRecente = historicoRecente;
    }
}
