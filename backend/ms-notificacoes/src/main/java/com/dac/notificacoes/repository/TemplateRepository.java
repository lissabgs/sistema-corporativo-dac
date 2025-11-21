package com.dac.notificacoes.repository;

import com.dac.notificacoes.model.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface TemplateRepository extends MongoRepository<Template, String> {
    Optional<Template> findByCodigo(String codigo);
}