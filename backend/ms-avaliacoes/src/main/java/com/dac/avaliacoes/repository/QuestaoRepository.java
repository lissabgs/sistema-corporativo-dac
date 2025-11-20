package com.dac.avaliacoes.repository;

import com.dac.avaliacoes.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {

    List<Questao> findByAvaliacaoId(Long avaliacaoId);
}