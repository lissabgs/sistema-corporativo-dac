package com.dac.notificacoes.repository;

import com.dac.notificacoes.model.FilaEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilaEmailRepository extends MongoRepository<FilaEmail, String> {
    // Métodos padrões de salvar já vêm prontos
}