package com.dac.cursos.service;

import com.dac.cursos.dto.CursoRequestDTO;
import com.dac.cursos.model.Aula;
import com.dac.cursos.model.Curso;
import com.dac.cursos.model.Modulo;
import com.dac.cursos.model.StatusCurso;
import com.dac.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays; // Necessário para o método listarDisponiveis
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Curso createCurso(CursoRequestDTO dto) {
        Curso curso = new Curso();
        preencherDadosCurso(curso, dto);
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso atualizarCurso(Long id, CursoRequestDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        preencherDadosCurso(curso, dto);
        return cursoRepository.save(curso);
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
    }

    @Transactional
    public void inativarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        curso.setStatus(StatusCurso.INATIVO);
        cursoRepository.save(curso);
    }

    @Transactional
    public Curso atualizarStatus(Long id, String novoStatusStr) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        StatusCurso novoStatus;
        try {
            novoStatus = StatusCurso.valueOf(novoStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + novoStatusStr);
        }

        curso.setStatus(novoStatus);
        return cursoRepository.save(curso);
    }

    public List<Curso> listarPorInstrutor(Long instrutorId) {
        return cursoRepository.findByInstrutorId(instrutorId);
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public List<Curso> listarDisponiveisParaAluno(String departamento, String nivelAluno) {
        List<String> niveisPermitidos;
        String nivel = nivelAluno != null ? nivelAluno.toUpperCase() : "INICIANTE";

        switch (nivel) {
            case "AVANCADO":
            case "AVANÇADO":
                niveisPermitidos = Arrays.asList("Iniciante", "Intermediário", "Avançado", "INICIANTE", "INTERMEDIARIO", "AVANCADO");
                break;
            case "INTERMEDIARIO":
            case "INTERMEDIÁRIO":
                niveisPermitidos = Arrays.asList("Iniciante", "Intermediário", "INICIANTE", "INTERMEDIARIO");
                break;
            default:
                niveisPermitidos = Arrays.asList("Iniciante", "INICIANTE");
                break;
        }

        return cursoRepository.buscarCursosDisponiveis(StatusCurso.ATIVO, departamento, niveisPermitidos);
    }

    private void preencherDadosCurso(Curso curso, CursoRequestDTO dto) {
        curso.setCodigo(dto.getCodigo());
        curso.setTitulo(dto.getTitulo());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoriaId(dto.getCategoriaId());
        curso.setInstrutorId(dto.getInstrutorId());
        curso.setDuracaoEstimada(dto.getDuracaoEstimada());
        curso.setNivelDificuldade(dto.getNivelDificuldade());

        // Define XP Total (Input do Usuário)
        int xpTotal = dto.getXpOferecido();
        curso.setXpOferecido(xpTotal);

        // --- CÁLCULO AUTOMÁTICO DOS CAMPOS DE XP ---

        // 1. XP Conclusão (50%)
        int xpConclusao = (int) (xpTotal * 0.50);
        curso.setXpConclusao(xpConclusao);

        // 2. XP Avaliação (25%)
        int xpAvaliacao = (int) (xpTotal * 0.25);
        curso.setXpAvaliacao(xpAvaliacao);

        // 3. XP Distribuído nas Aulas (25%)
        int xpParaAulas = (int) (xpTotal * 0.25);
        int xpPorAula = 0;

        if (dto.getPreRequisitos() != null) {
            curso.setPreRequisitos(new ArrayList<>(dto.getPreRequisitos()));
        } else {
            curso.setPreRequisitos(new ArrayList<>());
        }

        if (dto.getStatus() != null) {
            try {
                curso.setStatus(StatusCurso.valueOf(dto.getStatus().toUpperCase()));
            } catch (Exception e) {
                curso.setStatus(StatusCurso.RASCUNHO);
            }
        } else if (curso.getStatus() == null) {
            curso.setStatus(StatusCurso.RASCUNHO);
        }

        // Conta aulas para divisão
        long totalAulas = 0;
        if (dto.getModulos() != null) {
            totalAulas = dto.getModulos().stream()
                    .filter(m -> m.getAulas() != null)
                    .mapToLong(m -> m.getAulas().size())
                    .sum();
        }

        if (totalAulas > 0) {
            xpPorAula = xpParaAulas / (int) totalAulas;
        }

        // Conversão de Módulos e Aulas
        if (dto.getModulos() != null) {
            curso.getModulos().clear();
            int finalXpPorAula = xpPorAula;

            List<Modulo> novosModulos = dto.getModulos().stream().map(modDto -> {
                List<Aula> aulas = new ArrayList<>();
                if (modDto.getAulas() != null) {
                    aulas = modDto.getAulas().stream().map(aulaDto -> new Aula(
                            aulaDto.getTitulo(),
                            aulaDto.getDescricao(),
                            aulaDto.getUrlConteudo(),
                            aulaDto.getOrdem(),
                            aulaDto.isObrigatorio(),
                            finalXpPorAula // Usa o valor calculado da divisão
                    )).collect(Collectors.toList());
                }
                // Passa também o isObrigatorio do Módulo
                return new Modulo(modDto.getTitulo(), modDto.getOrdem(), modDto.isObrigatorio(), aulas);
            }).collect(Collectors.toList());

            curso.getModulos().addAll(novosModulos);
        }
    }
}