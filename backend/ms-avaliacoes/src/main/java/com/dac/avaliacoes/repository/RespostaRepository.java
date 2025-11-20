package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    List<Resposta> findByTentativaId(Long tentativaId);

    List<Resposta> findByQuestaoId(Long questaoId);
}