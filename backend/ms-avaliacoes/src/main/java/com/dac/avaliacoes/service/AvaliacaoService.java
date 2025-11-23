package com.dac.avaliacoes.service;

import com.dac.avaliacoes.model.*;
import com.dac.avaliacoes.repository.*;
import com.dac.avaliacoes.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // No service, injete o CursosClient e adicione o método
    @Autowired
    private CursosClient cursosClient; // Certifique-se que o FeignClient está configurado

    public List<Object> buscarCursosSemAvaliacao() {
        try {
            // 1. Busca TODOS os cursos do microsserviço de cursos
            List<Object> todosCursos = cursosClient.listarCursos();

            // 2. Busca IDs de cursos que JÁ têm avaliação
            List<Long> idsComAvaliacao = repository.findAll().stream()
                    .map(Avaliacao::getCursoId)
                    .collect(Collectors.toList());

            // 3. Filtra: Mantém apenas cursos cujo ID NÃO está na lista de avaliações
            // Obs: Assumindo que o objeto do curso vem como LinkedHashMap ou DTO.
            // Se tiver um DTO de curso compartilhado, use-o no lugar de Object.
            return todosCursos.stream()
                    .filter(curso -> {
                        // Conversão segura dependendo de como o Feign retorna (JSON)
                        // Se for um DTO Java, basta: !idsComAvaliacao.contains(curso.getId())
                        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) curso;
                        Integer id = (Integer) map.get("id");
                        return !idsComAvaliacao.contains(id.longValue());
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Fallback: se o serviço de cursos estiver fora, retorna lista vazia ou lança erro
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

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
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ========== BUSCAR POR ID ==========
    @Transactional(readOnly = true)
    public AvaliacaoDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        return converterParaDTO(avaliacao);
    }

    // ========== ATUALIZAR ==========
    @Transactional
    public AvaliacaoDTO atualizarAvaliacao(Long id, CriarAvaliacaoRequestDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        avaliacao.setTitulo(dto.getTitulo());
        avaliacao.setDescricao(dto.getDescricao());
        avaliacao.setTempoLimiteMinutos(dto.getTempoLimiteMinutos());
        avaliacao.setTentativasPermitidas(dto.getTentativasPermitidas());
        avaliacao.setNotaMinima(dto.getNotaMinima());

        Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return converterParaDTO(avaliacaoAtualizada);
    }

    // ========== DELETAR (Soft Delete) ==========
    @Transactional
    public void deletarAvaliacao(Long id) {
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