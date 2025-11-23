package com.dac.avaliacoes.service;

import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import com.dac.avaliacoes.dto.*;
import com.dac.avaliacoes.client.CursosClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList; // <--- Não esqueça deste import
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

    // ========== CRIAR AVALIAÇÃO ==========
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

        // --- CORREÇÃO: Processar e Salvar as Questões ---
        if (dto.getQuestoes() != null && !dto.getQuestoes().isEmpty()) {
            List<Questao> questoesEntidade = dto.getQuestoes().stream().map(qDto -> {
                Questao q = new Questao();
                q.setEnunciado(qDto.getEnunciado());
                q.setPeso(qDto.getPeso());

                // Converte String para Enum (OBJETIVA / DISCURSIVA)
                try {
                    q.setTipoQuestao(TipoQuestao.valueOf(qDto.getTipoQuestao()));
                } catch (Exception e) {
                    // Fallback seguro ou log de erro
                    q.setTipoQuestao(TipoQuestao.OBJETIVA);
                }

                q.setOpcoesResposta(qDto.getOpcoesResposta()); // O JSON das alternativas
                q.setRespostaCorreta(qDto.getRespostaCorreta());
                q.setOrdem(qDto.getOrdem());

                // VINCULA A QUESTÃO À AVALIAÇÃO (Importante para o OneToMany funcionar)
                q.setAvaliacao(avaliacao);

                return q;
            }).collect(Collectors.toList());

            avaliacao.setQuestoes(questoesEntidade);
        } else {
            avaliacao.setQuestoes(new ArrayList<>());
        }

        // O CascadeType.ALL na entidade Avaliacao vai salvar as questões automaticamente
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        logger.info(">>> [MS-AVALIACOES] Avaliação criada com ID: " + avaliacaoSalva.getId() + " e " + avaliacaoSalva.getQuestoes().size() + " questões.");

        return converterParaDTO(avaliacaoSalva);
    }

    // ========== LISTAR TODAS ==========
    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> listarTodas() {
        return avaliacaoRepository.findAll()
                .stream()
                .map(av -> {
                    AvaliacaoDTO dto = converterParaDTO(av);
                    boolean temTentativas = tentativaRepository.existsByAvaliacaoId(av.getId());
                    dto.setTemTentativas(temTentativas);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ========== BUSCAR CURSOS SEM AVALIAÇÃO ==========
    public List<Object> buscarCursosSemAvaliacao() {
        try {
            List<Object> todosCursos = cursosClient.listarCursos();
            List<Long> idsComAvaliacao = avaliacaoRepository.findAll().stream()
                    .map(Avaliacao::getCursoId)
                    .collect(Collectors.toList());

            return todosCursos.stream()
                    .filter(curso -> {
                        if (curso instanceof LinkedHashMap) {
                            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) curso;
                            Object idObj = map.get("id");
                            if (idObj instanceof Integer) {
                                return !idsComAvaliacao.contains(((Integer) idObj).longValue());
                            } else if (idObj instanceof Long) {
                                return !idsComAvaliacao.contains((Long) idObj);
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // ========== BUSCAR POR ID ==========
    @Transactional(readOnly = true)
    public AvaliacaoDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        AvaliacaoDTO dto = converterParaDTO(avaliacao);
        dto.setTemTentativas(tentativaRepository.existsByAvaliacaoId(id));
        return dto;
    }

    // ========== ATUALIZAR ==========
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

        // Para atualizar questões, o ideal seria limpar a lista atual e adicionar as novas
        // se o seu front envia a lista completa na edição.
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

    // ========== DELETAR ==========
    @Transactional
    public void deletarAvaliacao(Long id) {
        if (tentativaRepository.existsByAvaliacaoId(id)) {
            throw new RuntimeException("Não é possível excluir esta avaliação pois já existem alunos que a realizaram.");
        }
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        // Soft Delete
        avaliacao.setAtivo(false);
        avaliacaoRepository.save(avaliacao);
    }

    // ========== CONVERTER PARA DTO (ATUALIZADO) ==========
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

        // CORREÇÃO: Mapear as questões para enviar ao Frontend
        if (avaliacao.getQuestoes() != null) {
            List<QuestaoDTO> questoesDTO = avaliacao.getQuestoes().stream().map(q -> {
                QuestaoDTO qDto = new QuestaoDTO();
                qDto.setId(q.getId());
                qDto.setEnunciado(q.getEnunciado());
                qDto.setPeso(q.getPeso());
                // Converte Enum para String
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
}