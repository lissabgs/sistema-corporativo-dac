package com.dac.progresso.repository;

import com.dac.progresso.model.Progresso;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List; // <--- Importe List
import java.util.Optional;

public interface ProgressoRepository extends MongoRepository<Progresso, String> {
    
    Optional<Progresso> findByFuncionarioIdAndCursoId(Long funcionarioId, String cursoId);

    // NOVO: Listar todos os progressos de um aluno
    List<Progresso> findByFuncionarioId(Long funcionarioId);
}