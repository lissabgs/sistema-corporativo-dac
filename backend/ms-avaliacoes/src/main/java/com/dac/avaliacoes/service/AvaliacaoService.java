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
    private TentativaRepository tentativaRepository; // <--- Injeção Nova

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

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        logger.info(">>> [MS-AVALIACOES] Avaliação criada com ID: " + avaliacaoSalva.getId());

        return converterParaDTO(avaliacaoSalva);
    }

    // ========== LISTAR TODAS ==========
    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> listarTodas() {
        return avaliacaoRepository.findAll()
                .stream()
                .map(av -> {
                    AvaliacaoDTO dto = converterParaDTO(av);
                    // Verifica se existe alguma tentativa para esta avaliação
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
        // Também preenche no detalhe se tem tentativas
        dto.setTemTentativas(tentativaRepository.existsByAvaliacaoId(id));

        return dto;
    }

    // ========== ATUALIZAR ==========
    @Transactional
    public AvaliacaoDTO atualizarAvaliacao(Long id, CriarAvaliacaoRequestDTO dto) {
        // Validação de Segurança: Se já tem tentativas, não pode editar
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

        // Se quiser permitir atualizar questões, a lógica seria aqui,
        // mas com a trava acima, garantimos a integridade.

        Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return converterParaDTO(avaliacaoAtualizada);
    }

    // ========== DELETAR (Soft Delete) ==========
    @Transactional
    public void deletarAvaliacao(Long id) {
        // Validação de Segurança: Se já tem tentativas, não pode excluir
        if (tentativaRepository.existsByAvaliacaoId(id)) {
            throw new RuntimeException("Não é possível excluir esta avaliação pois já existem alunos que a realizaram.");
        }

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        avaliacao.setAtivo(false);
        avaliacaoRepository.save(avaliacao);
        logger.info(">>> [MS-AVALIACOES] Avaliação " + id + " deletada");
    }

    // ========== CONVERTER PARA DTO ==========
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
        return dto;
    }
}