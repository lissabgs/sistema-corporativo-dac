package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Tentativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface TentativaRepository extends JpaRepository<Tentativa, Long> {

    List<Tentativa> findByFuncionarioIdAndAvaliacaoId(Long funcionarioId, Long avaliacaoId);

    List<Tentativa> findByAvaliacaoId(Long avaliacaoId);

    List<Tentativa> findByFuncionarioId(Long funcionarioId);

    boolean existsByAvaliacaoId(Long avaliacaoId);

  // ADICIONE ESTE MÉTODO:
    @Query("SELECT t FROM Tentativa t WHERE t.avaliacao.curso.id = :cursoId ORDER BY t.dataFim DESC")
    List<Tentativa> findByAvaliacaoCursoId(@Param("cursoId") Long cursoId);
}