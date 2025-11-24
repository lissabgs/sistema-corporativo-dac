package com.dac.autenticacao.repository;

import com.dac.autenticacao.model.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// Mudou de JpaRepository<AuthUser, Long> para MongoRepository<AuthUser, String>
public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByEmail(String email);
}