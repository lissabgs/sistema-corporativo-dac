package com.dac.autenticacao.repository;

import com.dac.autenticacao.model.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser, String> {

    // Método para o Spring Data encontrar o usuário pelo email
    Optional<AuthUser> findByEmail(String email);
}