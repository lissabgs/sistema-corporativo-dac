package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Tentativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TentativaRepository extends JpaRepository<Tentativa, Long> {

    List<Tentativa> findByFuncionarioIdAndAvaliacaoId(Long funcionarioId, Long avaliacaoId);

    List<Tentativa> findByAvaliacaoId(Long avaliacaoId);

    List<Tentativa> findByFuncionarioId(Long funcionarioId);

    boolean existsByAvaliacaoId(Long avaliacaoId);
}
