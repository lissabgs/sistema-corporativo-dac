package com.dac.avaliacoes.service;

import com.dac.avaliacoes.client.*;
import com.dac.avaliacoes.dto.*;
import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardFuncionarioService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardFuncionarioService.class);

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private GamificacaoClient gamificacaoClient;

    @Autowired
    private ProgressoClient progressoClient;

    @Autowired
    private CursosClient cursosClient;

    /**
     * R15: Dashboard do Funcionário
     * Retorna todas as informações consolidadas do funcionário
     */
    @Transactional(readOnly = true)
    public DashboardFuncionarioDTO getDashboard(Long funcionarioId) {
        logger.info(">>> [MS-AVALIACOES] Gerando dashboard para funcionário ID: " + funcionarioId);

        DashboardFuncionarioDTO dashboard = new DashboardFuncionarioDTO();

        // 1. Dados do Funcionário (do MS-USUARIOS)
        try {
            Map<String, Object> funcionario = usuariosClient.buscarFuncionario(funcionarioId);
            dashboard.setNome((String) funcionario.get("nome"));
            dashboard.setEmail((String) funcionario.get("email"));
            dashboard.setDepartamento((String) funcionario.get("departamentoNome"));
        } catch (Exception e) {
            logger.error("Erro ao buscar dados do funcionário", e);
            dashboard.setNome("Desconhecido");
        }

        // 2. Dados de Gamificação (do MS-GAMIFICACAO)
        try {
            Map<String, Object> pontuacao = gamificacaoClient.buscarPontuacao(funcionarioId);
            dashboard.setXpTotal((Integer) pontuacao.getOrDefault("xpTotal", 0));
            dashboard.setNivel((String) pontuacao.getOrDefault("nivel", "Iniciante"));

            // Cálculo de progresso (exemplo: cada nível = 1000 XP)
            Integer xpAtual = dashboard.getXpTotal();
            Integer xpProximoNivel = 1000;
            Double progresso = (xpAtual % 1000) * 100.0 / 1000.0;
            dashboard.setXpProximoNivel(xpProximoNivel - (xpAtual % 1000));
            dashboard.setProgressoNivel(progresso);

            @SuppressWarnings("unchecked")
            List<String> badges = (List<String>) pontuacao.getOrDefault("badges", new ArrayList<>());
            dashboard.setBadges(badges);
        } catch (Exception e) {
            logger.warn("Erro ao buscar gamificação do funcionário", e);
            dashboard.setXpTotal(0);
            dashboard.setNivel("Iniciante");
            dashboard.setXpProximoNivel(1000);
            dashboard.setProgressoNivel(0.0);
            dashboard.setBadges(new ArrayList<>());
        }

        // 3. Estatísticas de Avaliações
        List<Tentativa> tentativas = tentativaRepository.findByFuncionarioId(funcionarioId);

        Integer totalAvaliacoes = tentativas.size();
        Integer aprovadas = 0;
        Integer reprovadas = 0;
        Double somaNotas = 0.0;

        for (Tentativa t : tentativas) {
            if (t.getStatus() == StatusTentativa.CONCLUIDA || t.getStatus() == StatusTentativa.CORRIGIDA) {
                somaNotas += t.getNotaObtida();
                if (t.getNotaObtida() >= t.getAvaliacao().getNotaMinima()) {
                    aprovadas++;
                } else {
                    reprovadas++;
                }
            }
        }

        Double mediaNotas = totalAvaliacoes > 0 ? somaNotas / totalAvaliacoes : 0.0;
        Double taxaAprovacao = totalAvaliacoes > 0 ? (aprovadas * 100.0) / totalAvaliacoes : 0.0;

        dashboard.setTotalAvaliacoesRealizadas(totalAvaliacoes);
        dashboard.setAvaliacoesAprovadas(aprovadas);
        dashboard.setAvaliacoesReprovadas(reprovadas);
        dashboard.setMediaNotasGeral(mediaNotas);
        dashboard.setTaxaAprovacao(taxaAprovacao);

        // 4. Cursos em Progresso (do MS-PROGRESSO)
        try {
            List<Map<String, Object>> progressos = progressoClient.buscarProgressoFuncionario(funcionarioId);
            List<CursoProgressoDTO> cursosProgresso = progressos.stream()
                    .map(this::converterParaCursoProgresso)
                    .collect(Collectors.toList());
            dashboard.setCursosEmProgresso(cursosProgresso);
        } catch (Exception e) {
            logger.warn("Erro ao buscar progresso de cursos", e);
            dashboard.setCursosEmProgresso(new ArrayList<>());
        }

        // 5. Avaliações Disponíveis
        List<AvaliacaoDisponivelDTO> avaliacoesDisponiveis = avaliacaoRepository.findByAtivo(true)
                .stream()
                .map(avaliacao -> converterParaAvaliacaoDisponivel(avaliacao, funcionarioId))
                .collect(Collectors.toList());
        dashboard.setAvaliacoesDisponiveis(avaliacoesDisponiveis);

        // 6. Histórico Recente (últimas 5 tentativas)
        List<TentativaHistoricoDTO> historico = tentativas.stream()
                .sorted((t1, t2) -> t2.getDataFim() != null && t1.getDataFim() != null
                        ? t2.getDataFim().compareTo(t1.getDataFim()) : 0)
                .limit(5)
                .map(this::converterParaHistorico)
                .collect(Collectors.toList());
        dashboard.setHistoricoRecente(historico);

        logger.info(">>> [MS-AVALIACOES] Dashboard do funcionário gerado com sucesso");
        return dashboard;
    }

    private CursoProgressoDTO converterParaCursoProgresso(Map<String, Object> progresso) {
        String cursoId = (String) progresso.get("cursoId");

        // Busca dados do curso no MS-CURSOS
        String titulo = "Curso não encontrado";
        String descricao = "";
        Integer totalAulas = 0;

        try {
            Map<String, Object> curso = cursosClient.buscarCurso(cursoId);
            titulo = (String) curso.get("titulo");
            descricao = (String) curso.get("descricao");

            @SuppressWarnings("unchecked")
            List<Object> aulas = (List<Object>) curso.get("aulas");
            totalAulas = aulas != null ? aulas.size() : 0;
        } catch (Exception e) {
            logger.warn("Erro ao buscar curso ID: " + cursoId);
        }

        @SuppressWarnings("unchecked")
        List<String> aulasConcluidas = (List<String>) progresso.getOrDefault("aulasConcluidas", new ArrayList<>());
        Integer numAulasConcluidas = aulasConcluidas.size();

        Double progressoPercentual = totalAulas > 0 ? (numAulasConcluidas * 100.0) / totalAulas : 0.0;

        return new CursoProgressoDTO(
                cursoId,
                titulo,
                descricao,
                totalAulas,
                numAulasConcluidas,
                progressoPercentual,
                0, // xpGanho (pode ser calculado depois)
                0  // xpTotal do curso
        );
    }

    private AvaliacaoDisponivelDTO converterParaAvaliacaoDisponivel(Avaliacao avaliacao, Long funcionarioId) {
        List<Tentativa> tentativasFeitas = tentativaRepository.findByFuncionarioIdAndAvaliacaoId(
                funcionarioId, avaliacao.getId());

        Integer tentativasRestantes = avaliacao.getTentativasPermitidas() - tentativasFeitas.size();
        if (tentativasRestantes < 0) tentativasRestantes = 0;

        String cursoTitulo = "Curso não definido";
        try {
            Map<String, Object> curso = cursosClient.buscarCurso(avaliacao.getCursoId().toString());
            cursoTitulo = (String) curso.get("titulo");
        } catch (Exception e) {
            logger.warn("Erro ao buscar título do curso");
        }

        return new AvaliacaoDisponivelDTO(
                avaliacao.getId(),
                avaliacao.getCodigo(),
                avaliacao.getTitulo(),
                avaliacao.getDescricao(),
                avaliacao.getTempoLimiteMinutos(),
                tentativasRestantes,
                avaliacao.getTentativasPermitidas(),
                avaliacao.getNotaMinima(),
                cursoTitulo
        );
    }

    private TentativaHistoricoDTO converterParaHistorico(Tentativa tentativa) {
        String status;
        if (tentativa.getStatus() == StatusTentativa.EM_PROGRESSO) {
            status = "EM_PROGRESSO";
        } else if (tentativa.getNotaObtida() >= tentativa.getAvaliacao().getNotaMinima()) {
            status = "APROVADO";
        } else {
            status = "REPROVADO";
        }

        return new TentativaHistoricoDTO(
                tentativa.getId(),
                tentativa.getAvaliacao().getTitulo(),
                tentativa.getDataFim(),
                tentativa.getNotaObtida(),
                tentativa.getAvaliacao().getNotaMinima(),
                status,
                tentativa.getNumeroTentativa()
        );
    }
}
