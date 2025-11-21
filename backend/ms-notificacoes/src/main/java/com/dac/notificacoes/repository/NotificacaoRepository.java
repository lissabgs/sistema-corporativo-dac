package com.dac.notificacoes.repository;

import com.dac.notificacoes.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {
    // O Spring cria automaticamente a busca pelo nome do método
    // Aqui: Buscar todas as notificações de um usuário específico
    List<Notificacao> findByUsuarioId(Long usuarioId);
}