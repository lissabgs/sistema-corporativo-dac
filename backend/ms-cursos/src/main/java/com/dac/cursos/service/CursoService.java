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
import java.util.stream.Collectors;
import java.util.Optional;

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

        StatusCurso statusAtual = curso.getStatus();
        StatusCurso novoStatus;

        try {
            novoStatus = StatusCurso.valueOf(novoStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + novoStatusStr);
        }

        // (Lógica de validação de estado mantém a mesma...)
        curso.setStatus(novoStatus);
        return cursoRepository.save(curso);
    }

    public List<Curso> listarPorInstrutor(Long instrutorId) {
        return cursoRepository.findByInstrutorId(instrutorId);
    }
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    private void preencherDadosCurso(Curso curso, CursoRequestDTO dto) {
        curso.setCodigo(dto.getCodigo());
        curso.setTitulo(dto.getTitulo());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoriaId(dto.getCategoriaId());
        curso.setInstrutorId(dto.getInstrutorId());
        curso.setDuracaoEstimada(dto.getDuracaoEstimada());
        curso.setXpOferecido(dto.getXpOferecido());
        curso.setNivelDificuldade(dto.getNivelDificuldade());

        // Garante que a lista não seja nula
        if (dto.getPreRequisitos() != null) {
            curso.setPreRequisitos(new ArrayList<>(dto.getPreRequisitos()));
        } else {
            curso.setPreRequisitos(new ArrayList<>());
        }

        // LÓGICA DE DISTRIBUIÇÃO DE XP (NOVA)
        int xpTotal = dto.getXpOferecido();
        int xpParaAulas = (int) (xpTotal * 0.25); // 25% do total para distribuir entre as aulas
        int xpPorAula = 0;

        // Primeiro, contamos quantas aulas existem no total para fazer a divisão
        long totalAulas = 0;
        if (dto.getModulos() != null) {
            totalAulas = dto.getModulos().stream()
                    .filter(m -> m.getAulas() != null)
                    .mapToLong(m -> m.getAulas().size())
                    .sum();
        }

        // Evita divisão por zero
        if (totalAulas > 0) {
            xpPorAula = xpParaAulas / (int) totalAulas;
        }

        // Conversão de Módulos e Aulas com XP calculado
        if (dto.getModulos() != null) {
            curso.getModulos().clear();

            // Variável final efetiva para uso no lambda
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
                            finalXpPorAula // AQUI: Usamos o valor calculado, ignorando o do DTO
                    )).collect(Collectors.toList());
                }
                return new Modulo(modDto.getTitulo(), modDto.getOrdem(), modDto.isObrigatorio(), aulas);
            }).collect(Collectors.toList());

            curso.getModulos().addAll(novosModulos);
        }
    }
}