package com.dac.cursos.controller;

import com.dac.cursos.dto.CursoRequestDTO;
import com.dac.cursos.model.Curso;
import com.dac.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // CRIAR
    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody CursoRequestDTO dto) {
        Curso novoCurso = cursoService.createCurso(dto);
        return new ResponseEntity<>(novoCurso, HttpStatus.CREATED);
    }

    // ATUALIZAR (Edita tudo)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCurso(@PathVariable Long id, @RequestBody CursoRequestDTO dto) {
        try {
            Curso cursoAtualizado = cursoService.atualizarCurso(id, dto);
            return ResponseEntity.ok(cursoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        List<Curso> cursos = cursoService.listarTodos();
        return ResponseEntity.ok(cursos);
    }

    // LISTAR POR INSTRUTOR
    @GetMapping("/instrutor/{instrutorId}")
    public ResponseEntity<List<Curso>> listarPorInstrutor(@PathVariable Long instrutorId) {
        List<Curso> cursos = cursoService.listarPorInstrutor(instrutorId);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Curso>> listarCursosDisponiveis(
            @RequestParam String departamento,
            @RequestParam String nivel) {

        List<Curso> cursos = cursoService.listarDisponiveisParaAluno(departamento, nivel);
        return ResponseEntity.ok(cursos);
    }

    // NOVO: DELETE Lógico (Muda status para INATIVO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id) {
        cursoService.inativarCurso(id);
        return ResponseEntity.noContent().build();
    }
}