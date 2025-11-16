package com.dac.cursos.service;

import com.dac.cursos.dto.AulaDTO;
import com.dac.cursos.dto.CursoRequestDTO;
import com.dac.cursos.model.Aula;
import com.dac.cursos.model.Curso;
import com.dac.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // <-- IMPORT QUE FALTAVA
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Curso createCurso(CursoRequestDTO dto) {

        // 1. Converte a lista de DTOs de Aulas em Modelos de Aulas
        List<Aula> aulas = dto.getAulas().stream()
                .map(aulaDto -> new Aula(
                        aulaDto.getTitulo(),
                        aulaDto.getDescricao(),
                        aulaDto.getUrlConteudo(),
                        aulaDto.getOrdem()
                ))
                .collect(Collectors.toList());

        // 2. Cria a entidade Curso com os dados do DTO
        Curso curso = new Curso();
        curso.setCodigo(dto.getCodigo());
        curso.setTitulo(dto.getTitulo());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoriaId(dto.getCategoriaId());
        curso.setInstrutorId(dto.getInstrutorId());
        curso.setDuracaoEstimada(dto.getDuracaoEstimada());
        curso.setXpOferecido(dto.getXpOferecido());
        curso.setNivelDificuldade(dto.getNivelDificuldade());
        curso.setAtivo(dto.isAtivo());
        curso.setPreRequisitos(dto.getPreRequisitos());

        // 3. Adiciona a lista de aulas convertida
        curso.setAulas(aulas);

        // 4. Salva no MongoDB
        return cursoRepository.save(curso);
    }
}