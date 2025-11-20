package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Correcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CorrecaoRepository extends JpaRepository<Correcao, Long> {

    List<Correcao> findByTentativaId(Long tentativaId);

    List<Correcao> findByStatus(String status);

    List<Correcao> findByInstrutorId(Long instrutorId);
}