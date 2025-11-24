package com.dac.cursos.repository;

import com.dac.cursos.model.Curso;
import com.dac.cursos.model.StatusCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByInstrutorId(Long instrutorId);

    @Query("SELECT c FROM Curso c WHERE c.status = :status AND c.categoriaId = :departamento AND c.nivelDificuldade IN :niveis")
    List<Curso> buscarCursosDisponiveis(
            @Param("status") StatusCurso status,
            @Param("departamento") String departamento,
            @Param("niveis") List<String> niveis
    );

    @Query("SELECT c FROM Curso c WHERE c.status = :status AND c.categoriaId = :departamento AND c.nivelDificuldade IN :niveis AND c.codigo NOT IN :matriculados")
    List<Curso> buscarCursosDisponiveisFiltrados(
            @Param("status") StatusCurso status,
            @Param("departamento") String departamento,
            @Param("niveis") List<String> niveis,
            @Param("matriculados") List<String> matriculados
    );
}