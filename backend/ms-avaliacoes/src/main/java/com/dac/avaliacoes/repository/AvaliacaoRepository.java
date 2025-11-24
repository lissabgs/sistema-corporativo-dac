package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findByCodigo(String codigo);

    List<Avaliacao> findByCursoId(Long cursoId);

    List<Avaliacao> findByCursoIdIn(List<String> cursoIds);
    List<Avaliacao> findByAtivo(Boolean ativo);
}
