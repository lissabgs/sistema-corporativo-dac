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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardAdminService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardAdminService.class);

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private UsuariosClient usuariosClient;

    @Autowired
    private CursosClient cursosClient;

    @Autowired
    private GamificacaoClient gamificacaoClient;

    /**
     * R16: Dashboard Administrativo
     */
    @Transactional(readOnly = true)
    public DashboardAdminDTO getDashboard() {
        logger.info(">>> [MS-AVALIACOES] Gerando dashboard administrativo");

        DashboardAdminDTO dashboard = new DashboardAdminDTO();

        // 1. Estatísticas Gerais
        dashboard.setTotalAvaliacoes(avaliacaoRepository.count());
        dashboard.setTotalTentativas(tentativaRepository.count());

        // Buscar totais de outros microsserviços (simulado)
        dashboard.setTotalFuncionarios(100L); // Buscar do MS-USUARIOS
        dashboard.setTotalInstrutores(10L);   // Buscar do MS-USUARIOS
        dashboard.setTotalCursos(50L);        // Buscar do MS-CURSOS

        // 2. Métricas de Desempenho
        List<Tentativa> todasTentativas = tentativaRepository.findAll();

        Double somaNotas = todasTentativas.stream()
                .filter(t -> t.getStatus() == StatusTentativa.CONCLUIDA || t.getStatus() == StatusTentativa.CORRIGIDA)
                .mapToDouble(Tentativa::getNotaObtida)
                .sum();

        Long totalConcluidas = todasTentativas.stream()
                .filter(t -> t.getStatus() == StatusTentativa.CONCLUIDA || t.getStatus() == StatusTentativa.CORRIGIDA)
                .count();

        Double mediaGeral = totalConcluidas > 0 ? somaNotas / totalConcluidas : 0.0;
        dashboard.setMediaNotasGeral(mediaGeral);

        Long aprovadas = todasTentativas.stream()
                .filter(t -> t.getNotaObtida() >= t.getAvaliacao().getNotaMinima())
                .count();

        Double taxaAprovacao = totalConcluidas > 0 ? (aprovadas * 100.0) / totalConcluidas : 0.0;
        dashboard.setTaxaAprovacaoGeral(taxaAprovacao);
        dashboard.setTaxaConclusaoCursos(75.0); // Mock (buscar do MS-PROGRESSO)

        // 3. Top Performers
        dashboard.setTopFuncionarios(buscarTopFuncionarios());
        dashboard.setTopCursos(buscarTopCursos());
        dashboard.setTopAvaliacoes(buscarTopAvaliacoes());

        // 4. Estatísticas por Departamento
        dashboard.setEstatisticasPorDepartamento(calcularEstatisticasDepartamentos());

        // 5. Tendências (últimos 30 dias)
        dashboard.setTentativasPorDia(calcularTentativasPorDia());
        dashboard.setMediaNotasPorDia(calcularMediaNotasPorDia());

        logger.info(">>> [MS-AVALIACOES] Dashboard administrativo gerado com sucesso");
        return dashboard;
    }

    /**
     * R17: Relatório Geral
     */
    @Transactional(readOnly = true)
    public RelatorioGeralDTO gerarRelatorioGeral(LocalDateTime dataInicio, LocalDateTime dataFim) {
        logger.info(">>> [MS-AVALIACOES] Gerando relatório geral: {} a {}", dataInicio, dataFim);

        RelatorioGeralDTO relatorio = new RelatorioGeralDTO();
        relatorio.setDataGeracao(LocalDateTime.now());
        relatorio.setPeriodo(dataInicio + " a " + dataFim);

        // Filtrar tentativas por período
        List<Tentativa> tentativas = tentativaRepository.findAll().stream()
                .filter(t -> t.getDataInicio().isAfter(dataInicio) && t.getDataInicio().isBefore(dataFim))
                .collect(Collectors.toList());

        // Resumo Executivo
        relatorio.setTotalAvaliacoesRealizadas(tentativas.size());

        Long funcionariosAtivos = tentativas.stream()
                .map(Tentativa::getFuncionarioId)
                .distinct()
                .count();
        relatorio.setTotalFuncionariosAtivos(funcionariosAtivos.intValue());

        Double mediaNotas = tentativas.stream()
                .mapToDouble(Tentativa::getNotaObtida)
                .average()
                .orElse(0.0);
        relatorio.setMediaNotasGeral(mediaNotas);

        Long aprovadas = tentativas.stream()
                .filter(t -> t.getNotaObtida() >= t.getAvaliacao().getNotaMinima())
                .count();
        Double taxaAprovacao = tentativas.size() > 0 ? (aprovadas * 100.0) / tentativas.size() : 0.0;
        relatorio.setTaxaAprovacao(taxaAprovacao);

        // Detalhamento por Avaliação
        relatorio.setDetalhePorAvaliacao(gerarRelatoriosPorAvaliacao(tentativas));

        // Detalhamento por Funcionário
        relatorio.setDetalhePorFuncionario(gerarRelatoriosPorFuncionario(tentativas));

        logger.info(">>> [MS-AVALIACOES] Relatório gerado com sucesso");
        return relatorio;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private List<Map<String, Object>> buscarTopFuncionarios() {
        // Mock - Integrar com MS-GAMIFICACAO para buscar top por XP
        return List.of(
                Map.of("nome", "João Silva", "xp", 5000, "nivel", "Expert"),
                Map.of("nome", "Maria Santos", "xp", 4500, "nivel", "Avançado")
        );
    }

    private List<Map<String, Object>> buscarTopCursos() {
        // Mock - Integrar com MS-PROGRESSO para buscar cursos mais realizados
        return List.of(
                Map.of("titulo", "Java Avançado", "totalAlunos", 50),
                Map.of("titulo", "Spring Boot", "totalAlunos", 45)
        );
    }

    private List<Map<String, Object>> buscarTopAvaliacoes() {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAll();

        return avaliacoes.stream()
                .map(avaliacao -> {
                    Integer total = tentativaRepository.findByAvaliacaoId(avaliacao.getId()).size();
                    return Map.of(
                            "titulo", (Object) avaliacao.getTitulo(),
                            "totalRealizacoes", (Object) total
                    );
                })
                .sorted((m1, m2) -> ((Integer) m2.get("totalRealizacoes")).compareTo((Integer) m1.get("totalRealizacoes")))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<EstatisticaDepartamentoDTO> calcularEstatisticasDepartamentos() {
        // Mock - Integrar com MS-USUARIOS para buscar departamentos
        return List.of(
                new EstatisticaDepartamentoDTO("TI", 30, 150, 7.5, 80.0, 15000),
                new EstatisticaDepartamentoDTO("RH", 20, 100, 8.0, 85.0, 10000)
        );
    }

    private Map<String, Integer> calcularTentativasPorDia() {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        // Lógica para agrupar por dia (últimos 30 dias)
        resultado.put("2025-01-01", 10);
        resultado.put("2025-01-02", 15);
        return resultado;
    }

    private Map<String, Double> calcularMediaNotasPorDia() {
        Map<String, Double> resultado = new LinkedHashMap<>();
        resultado.put("2025-01-01", 7.5);
        resultado.put("2025-01-02", 8.0);
        return resultado;
    }

    private List<RelatorioAvaliacaoDTO> gerarRelatoriosPorAvaliacao(List<Tentativa> tentativas) {
        Map<Long, List<Tentativa>> porAvaliacao = tentativas.stream()
                .collect(Collectors.groupingBy(t -> t.getAvaliacao().getId()));

        return porAvaliacao.entrySet().stream()
                .map(entry -> {
                    Avaliacao avaliacao = entry.getValue().get(0).getAvaliacao();
                    List<Tentativa> tentativasAvaliacao = entry.getValue();

                    Integer total = tentativasAvaliacao.size();
                    Integer aprovados = (int) tentativasAvaliacao.stream()
                            .filter(t -> t.getNotaObtida() >= avaliacao.getNotaMinima())
                            .count();
                    Integer reprovados = total - aprovados;

                    Double media = tentativasAvaliacao.stream()
                            .mapToDouble(Tentativa::getNotaObtida)
                            .average()
                            .orElse(0.0);

                    Double taxa = total > 0 ? (aprovados * 100.0) / total : 0.0;

                    return new RelatorioAvaliacaoDTO(
                            avaliacao.getCodigo(),
                            avaliacao.getTitulo(),
                            "Curso Mock", // Buscar do MS-CURSOS
                            total,
                            aprovados,
                            reprovados,
                            media,
                            taxa
                    );
                })
                .collect(Collectors.toList());
    }

    private List<RelatorioFuncionarioDTO> gerarRelatoriosPorFuncionario(List<Tentativa> tentativas) {
        Map<Long, List<Tentativa>> porFuncionario = tentativas.stream()
                .collect(Collectors.groupingBy(Tentativa::getFuncionarioId));

        return porFuncionario.entrySet().stream()
                .map(entry -> {
                    Long funcionarioId = entry.getKey();
                    List<Tentativa> tentativasFuncionario = entry.getValue();

                    String nome = "Funcionário " + funcionarioId;
                    String departamento = "TI";

                    // Buscar dados reais do MS-USUARIOS
                    try {
                        Map<String, Object> func = usuariosClient.buscarFuncionario(funcionarioId);
                        nome = (String) func.get("nome");
                        departamento = (String) func.get("departamentoNome");
                    } catch (Exception e) {
                        logger.warn("Erro ao buscar funcionário ID: " + funcionarioId);
                    }

                    Integer total = tentativasFuncionario.size();
                    Integer aprovacoes = (int) tentativasFuncionario.stream()
                            .filter(t -> t.getNotaObtida() >= t.getAvaliacao().getNotaMinima())
                            .count();

                    Double media = tentativasFuncionario.stream()
                            .mapToDouble(Tentativa::getNotaObtida)
                            .average()
                            .orElse(0.0);

                    return new RelatorioFuncionarioDTO(
                            nome,
                            departamento,
                            total,
                            aprovacoes,
                            media,
                            0, // XP - buscar do MS-GAMIFICACAO
                            "Iniciante"
                    );
                })
                .collect(Collectors.toList());
    }
}
