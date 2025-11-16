package com.dac.cursos.controller;

import com.dac.cursos.dto.CursoRequestDTO;
import com.dac.cursos.model.Curso;
import com.dac.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * R09: Cadastro de Curso
     * (Assume-se que será protegido por INSTRUTOR/ADMIN no futuro)
     */
    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody CursoRequestDTO dto) {
        Curso novoCurso = cursoService.createCurso(dto);
        return new ResponseEntity<>(novoCurso, HttpStatus.CREATED);
    }

    // (Aqui você adicionará os outros endpoints:
    // @GetMapping para R04, @PutMapping para R10, etc.)
}