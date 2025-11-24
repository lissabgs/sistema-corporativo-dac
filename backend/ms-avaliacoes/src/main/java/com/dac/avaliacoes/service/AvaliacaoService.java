package com.dac.avaliacoes.service;

import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import com.dac.avaliacoes.dto.*;
import com.dac.avaliacoes.client.CursosClient;
import com.dac.avaliacoes.client.ProgressoClient; // <--- Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;

@Data
@Service
public class AvaliacaoService {

    private static final Logger logger = LoggerFactory.getLogger(AvaliacaoService.class);

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private TentativaRepository tentativaRepository;

    @Autowired
    private CursosClient cursosClient;

    @Autowired
    private ProgressoClient progressoClient; // <--- Client do Progresso

    // ========== MÉTODOS NOVOS (PENDENTES, AGUARDANDO, CONCLUÍDAS) ==========

    public List<AvaliacaoDTO> listarPendentes(Long funcionarioId) {
        List<String> cursosConcluidosIds = new ArrayList<>();
        try {
            cursosConcluidosIds = progressoClient.obterCursosConcluidos(funcionarioId);
        } catch (Exception e) {
            logger.error("Erro ao buscar progresso: " + e.getMessage());
            return new ArrayList<>();
        }

        if (cursosConcluidosIds.isEmpty()) return new ArrayList<>();

        // Busca tentativas já aprovadas ou em análise
        List<Tentativa> tentativasAluno = tentativaRepository.findByFuncionarioId(funcionarioId);
        List<Long> avaliacoesResolvidas = tentativasAluno.stream()
                .filter(t -> t.getStatus() == StatusTentativa.APROVADO ||
                        t.getStatus() == StatusTentativa.EM_ANALISE ||
                        t.getStatus() == StatusTentativa.AGUARDANDO_CORRECAO)
                .map(t -> t.getAvaliacao().getId())
                .collect(Collectors.toList());

        // Busca avaliações dos cursos concluídos
        List<Avaliacao> avaliacoesDisponiveis = avaliacaoRepository.findByCursoIdIn(cursosConcluidosIds);

        // Retorna apenas as que ainda não foram resolvidas
        return avaliacoesDisponiveis.stream()
                .filter(av -> !avaliacoesResolvidas.contains(av.getId()))
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TentativaDTO> listarAguardandoCorrecao(Long funcionarioId) {
        return tentativaRepository.findByFuncionarioId(funcionarioId).stream()
                .filter(t -> t.getStatus() == StatusTentativa.AGUARDANDO_CORRECAO ||
                        t.getStatus() == StatusTentativa.EM_ANALISE)
                .map(this::converterTentativaParaDTO)
                .collect(Collectors.toList());
    }

    public List<TentativaDTO> listarConcluidas(Long funcionarioId) {
        return tentativaRepository.findByFuncionarioId(funcionarioId).stream()
                .filter(t -> t.getStatus() == StatusTentativa.APROVADO ||
                        t.getStatus() == StatusTentativa.REPROVADO)
                .map(this::converterTentativaParaDTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS CRUD (JÁ EXISTENTES) ==========

    @Transactional
    public AvaliacaoDTO criarAvaliacao(CriarAvaliacaoRequestDTO dto) {
        logger.info(">>> [MS-AVALIACOES] Criando nova avaliação: " + dto.getTitulo());

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCodigo(dto.getCodigo());
        avaliacao.setTitulo(dto.getTitulo());
        avaliacao.setDescricao(dto.getDescricao());
        avaliacao.setCursoId(dto.getCursoId());
        avaliacao.setTempoLimiteMinutos(dto.getTempoLimiteMinutos());
        avaliacao.setTentativasPermitidas(dto.getTentativasPermitidas());
        avaliacao.setNotaMinima(dto.getNotaMinima());
        avaliacao.setAtivo(true);

        if (dto.getQuestoes() != null && !dto.getQuestoes().isEmpty()) {
            List<Questao> questoesEntidade = dto.getQuestoes().stream().map(qDto -> {
                Questao q = new Questao();
                q.setEnunciado(qDto.getEnunciado());
                q.setPeso(qDto.getPeso());
                try { q.setTipoQuestao(TipoQuestao.valueOf(qDto.getTipoQuestao())); }
                catch (Exception e) { q.setTipoQuestao(TipoQuestao.OBJETIVA); }
                q.setOpcoesResposta(qDto.getOpcoesResposta());
                q.setRespostaCorreta(qDto.getRespostaCorreta());
                q.setOrdem(qDto.getOrdem());
                q.setAvaliacao(avaliacao);
                return q;
            }).collect(Collectors.toList());
            avaliacao.setQuestoes(questoesEntidade);
        } else {
            avaliacao.setQuestoes(new ArrayList<>());
        }

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        return converterParaDTO(avaliacaoSalva);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> listarTodas() {
        return avaliacaoRepository.findAll().stream()
                .map(av -> {
                    AvaliacaoDTO dto = converterParaDTO(av);
                    dto.setTemTentativas(tentativaRepository.existsByAvaliacaoId(av.getId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<Object> buscarCursosSemAvaliacao() {
        try {
            List<Object> todosCursos = cursosClient.listarCursos();
            List<Long> idsComAvaliacao = avaliacaoRepository.findAll().stream()
                    .map(Avaliacao::getCursoId).collect(Collectors.toList());

            return todosCursos.stream().filter(curso -> {
                if (curso instanceof LinkedHashMap) {
                    LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) curso;
                    Object idObj = map.get("id");
                    if (idObj instanceof Integer) return !idsComAvaliacao.contains(((Integer) idObj).longValue());
                    else if (idObj instanceof Long) return !idsComAvaliacao.contains((Long) idObj);
                }
                return true;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public AvaliacaoDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        AvaliacaoDTO dto = converterParaDTO(avaliacao);
        dto.setTemTentativas(tentativaRepository.existsByAvaliacaoId(id));
        return dto;
    }

    @Transactional
    public AvaliacaoDTO atualizarAvaliacao(Long id, CriarAvaliacaoRequestDTO dto) {
        if (tentativaRepository.existsByAvaliacaoId(id)) {
            throw new RuntimeException("Não é possível editar esta avaliação pois já existem alunos que a realizaram.");
        }
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        avaliacao.setTitulo(dto.getTitulo());
        avaliacao.setDescricao(dto.getDescricao());
        avaliacao.setTempoLimiteMinutos(dto.getTempoLimiteMinutos());
        avaliacao.setTentativasPermitidas(dto.getTentativasPermitidas());
        avaliacao.setNotaMinima(dto.getNotaMinima());

        if (dto.getQuestoes() != null) {
            avaliacao.getQuestoes().clear();
            List<Questao> novasQuestoes = dto.getQuestoes().stream().map(qDto -> {
                Questao q = new Questao();
                q.setEnunciado(qDto.getEnunciado());
                q.setPeso(qDto.getPeso());
                try { q.setTipoQuestao(TipoQuestao.valueOf(qDto.getTipoQuestao())); }
                catch (Exception e) { q.setTipoQuestao(TipoQuestao.OBJETIVA); }
                q.setOpcoesResposta(qDto.getOpcoesResposta());
                q.setRespostaCorreta(qDto.getRespostaCorreta());
                q.setOrdem(qDto.getOrdem());
                q.setAvaliacao(avaliacao);
                return q;
            }).collect(Collectors.toList());
            avaliacao.getQuestoes().addAll(novasQuestoes);
        }
        Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return converterParaDTO(avaliacaoAtualizada);
    }

    @Transactional
    public void deletarAvaliacao(Long id) {
        if (tentativaRepository.existsByAvaliacaoId(id)) {
            throw new RuntimeException("Não é possível excluir esta avaliação pois já existem alunos que a realizaram.");
        }
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        avaliacao.setAtivo(false);
        avaliacaoRepository.save(avaliacao);
    }

    // AUXILIARES
    private AvaliacaoDTO converterParaDTO(Avaliacao avaliacao) {
        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setId(avaliacao.getId());
        dto.setCodigo(avaliacao.getCodigo());
        dto.setTitulo(avaliacao.getTitulo());
        dto.setDescricao(avaliacao.getDescricao());
        dto.setCursoId(avaliacao.getCursoId());
        dto.setTempoLimiteMinutos(avaliacao.getTempoLimiteMinutos());
        dto.setTentativasPermitidas(avaliacao.getTentativasPermitidas());
        dto.setNotaMinima(avaliacao.getNotaMinima());
        dto.setAtivo(avaliacao.getAtivo());
        dto.setDataCriacao(avaliacao.getDataCriacao());

        if (avaliacao.getQuestoes() != null) {
            List<QuestaoDTO> questoesDTO = avaliacao.getQuestoes().stream().map(q -> {
                QuestaoDTO qDto = new QuestaoDTO();
                qDto.setId(q.getId());
                qDto.setEnunciado(q.getEnunciado());
                qDto.setPeso(q.getPeso());
                qDto.setTipoQuestao(q.getTipoQuestao() != null ? q.getTipoQuestao().name() : "OBJETIVA");
                qDto.setOpcoesResposta(q.getOpcoesResposta());
                qDto.setRespostaCorreta(q.getRespostaCorreta());
                qDto.setOrdem(q.getOrdem());
                return qDto;
            }).collect(Collectors.toList());
            dto.setQuestoes(questoesDTO);
        }
        return dto;
    }

    private TentativaDTO converterTentativaParaDTO(Tentativa t) {
        TentativaDTO dto = new TentativaDTO();
        dto.setId(t.getId());
        dto.setDataTentativa(t.getDataInicio());
        dto.setNota(t.getNotaFinal());
        dto.setStatus(t.getStatus().toString());
        dto.setAvaliacaoTitulo(t.getAvaliacao() != null ? t.getAvaliacao().getTitulo() : "Sem título");
        return dto;
    }
}