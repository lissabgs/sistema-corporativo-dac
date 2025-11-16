package com.dac.cursos.repository;

import com.dac.cursos.model.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends MongoRepository<Curso, String> {
    // O MongoRepository já nos dá o save(), findAll(), findById(), delete() etc.
}