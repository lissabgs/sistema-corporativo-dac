package com.dac.avaliacoes.service;

import com.dac.avaliacoes.client.UsuariosClient;
import com.dac.avaliacoes.dto.*;
import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardInstrutorService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardInstrutorService.class);

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private CorrecaoRepository correcaoRepository;

    @Autowired
    private UsuariosClient usuariosClient;

    /**
     * R14: Dashboard do Instrutor
     * Retorna estatísticas e dados consolidados para o instrutor
     */
    @Transactional(readOnly = true)
    public DashboardInstrutorDTO getDashboard(Long instrutorId) {
        logger.info(">>> [MS-AVALIACOES] Gerando dashboard para instrutor ID: " + instrutorId);

        DashboardInstrutorDTO dashboard = new DashboardInstrutorDTO();

        // 1. Total de Avaliações Criadas
        Long totalAvaliacoes = avaliacaoRepository.count();
        dashboard.setTotalAvaliacoesCriadas(totalAvaliacoes);

        // 2. Total de Tentativas Realizadas
        Long totalTentativas = tentativaRepository.count();
        dashboard.setTotalTentativasRealizadas(totalTentativas);

        // 3. Tentativas Pendentes de Correção
        List<Correcao> pendentes = correcaoRepository.findByStatus("PENDENTE");
        dashboard.setTentativasPendentesCorrecao((long) pendentes.size());

        // 4. Média Geral de Notas
        List<Tentativa> todasTentativas = tentativaRepository.findAll();
        Double mediaGeral = todasTentativas.stream()
                .filter(t -> t.getStatus() == StatusTentativa.CONCLUIDA || t.getStatus() == StatusTentativa.CORRIGIDA)
                .mapToDouble(Tentativa::getNotaObtida)
                .average()
                .orElse(0.0);
        dashboard.setMediaNotasGeral(mediaGeral);

        // 5. Avaliações Recentes (últimas 5)
        List<AvaliacaoResumoDTO> avaliacoesRecentes = avaliacaoRepository.findAll()
                .stream()
                .sorted((a1, a2) -> a2.getDataCriacao().compareTo(a1.getDataCriacao()))
                .limit(5)
                .map(this::converterParaResumo)
                .collect(Collectors.toList());
        dashboard.setAvaliacoesRecentes(avaliacoesRecentes);

        // 6. Tentativas Pendentes (com dados do funcionário)
        List<TentativaPendenteDTO> tentativasPendentes = tentativaRepository.findAll()
                .stream()
                .filter(t -> t.getStatus() == StatusTentativa.CONCLUIDA)
                .limit(10)
                .map(this::converterParaTentativaPendente)
                .collect(Collectors.toList());
        dashboard.setTentativasPendentes(tentativasPendentes);

        // 7. Estatísticas por Avaliação
        List<EstatisticaAvaliacaoDTO> estatisticas = avaliacaoRepository.findAll()
                .stream()
                .map(this::calcularEstatisticasAvaliacao)
                .collect(Collectors.toList());
        dashboard.setEstatisticasPorAvaliacao(estatisticas);

        logger.info(">>> [MS-AVALIACOES] Dashboard gerado com sucesso");
        return dashboard;
    }

    private AvaliacaoResumoDTO converterParaResumo(Avaliacao avaliacao) {
        List<Tentativa> tentativas = tentativaRepository.findByAvaliacaoId(avaliacao.getId());

        Integer totalTentativas = tentativas.size();
        Double mediaNotas = tentativas.stream()
                .filter(t -> t.getStatus() == StatusTentativa.CONCLUIDA || t.getStatus() == StatusTentativa.CORRIGIDA)
                .mapToDouble(Tentativa::getNotaObtida)
                .average()
                .orElse(0.0);

        return new AvaliacaoResumoDTO(
                avaliacao.getId(),
                avaliacao.getCodigo(),
                avaliacao.getTitulo(),
                avaliacao.getCursoId(),
                totalTentativas,
                mediaNotas,
                avaliacao.getDataCriacao()
        );
    }

    private TentativaPendenteDTO converterParaTentativaPendente(Tentativa tentativa) {
        String funcionarioNome = "Desconhecido";

        try {
            Map<String, Object> funcionario = usuariosClient.buscarFuncionario(tentativa.getFuncionarioId());
            if (funcionario != null && funcionario.get("nome") != null) {
                funcionarioNome = (String) funcionario.get("nome");
            }
        } catch (Exception e) {
            logger.warn("Não foi possível buscar dados do funcionário ID: " + tentativa.getFuncionarioId());
        }

        // AQUI ESTAVA O ERRO: Adicionei o 7º argumento (Status)
        return new TentativaPendenteDTO(
                tentativa.getId(),
                tentativa.getFuncionarioId(),
                funcionarioNome,
                tentativa.getAvaliacao().getTitulo(),
                tentativa.getDataFim(),
                tentativa.getNotaObtida(),
                tentativa.getStatus().toString() // <--- ADICIONADO
        );
    }

    private EstatisticaAvaliacaoDTO calcularEstatisticasAvaliacao(Avaliacao avaliacao) {
        List<Tentativa> tentativas = tentativaRepository.findByAvaliacaoId(avaliacao.getId());

        Integer totalTentativas = tentativas.size();
        Integer aprovadas = (int) tentativas.stream()
                .filter(t -> t.getNotaObtida() >= avaliacao.getNotaMinima())
                .count();
        Integer reprovadas = totalTentativas - aprovadas;

        Double mediaNotas = tentativas.stream()
                .mapToDouble(Tentativa::getNotaObtida)
                .average()
                .orElse(0.0);

        Double notaMin = tentativas.stream()
                .mapToDouble(Tentativa::getNotaObtida)
                .min()
                .orElse(0.0);

        Double notaMax = tentativas.stream()
                .mapToDouble(Tentativa::getNotaObtida)
                .max()
                .orElse(0.0);

        Double taxaAprovacao = totalTentativas > 0 ? (aprovadas * 100.0) / totalTentativas : 0.0;

        return new EstatisticaAvaliacaoDTO(
                avaliacao.getId(),
                avaliacao.getTitulo(),
                totalTentativas,
                aprovadas,
                reprovadas,
                mediaNotas,
                notaMin,
                notaMax,
                taxaAprovacao
        );
    }
}