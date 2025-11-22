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

        try {
            if (dto.getStatus() != null) {
                curso.setStatus(StatusCurso.valueOf(dto.getStatus().toUpperCase()));
            } else {
                if (curso.getStatus() == null) curso.setStatus(StatusCurso.RASCUNHO);
            }
        } catch (IllegalArgumentException e) {
            curso.setStatus(StatusCurso.RASCUNHO);
        }

        // Conversão de Módulos e Aulas
        if (dto.getModulos() != null) {
            // Limpa a lista atual para substituir (se for update)
            curso.getModulos().clear();

            List<Modulo> novosModulos = dto.getModulos().stream().map(modDto -> {
                List<Aula> aulas = new ArrayList<>();
                if (modDto.getAulas() != null) {
                    aulas = modDto.getAulas().stream().map(aulaDto -> new Aula(
                            aulaDto.getTitulo(),
                            aulaDto.getDescricao(),
                            aulaDto.getUrlConteudo(),
                            aulaDto.getOrdem(),
                            aulaDto.isObrigatorio(),
                            aulaDto.getXpModulo()
                    )).collect(Collectors.toList());
                }
                return new Modulo(modDto.getTitulo(), modDto.getOrdem(), aulas);
            }).collect(Collectors.toList());

            curso.getModulos().addAll(novosModulos);
        }
    }
}