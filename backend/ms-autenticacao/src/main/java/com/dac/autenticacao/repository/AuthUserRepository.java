package com.dac.autenticacao.repository;

import com.dac.autenticacao.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository; // Mudou para JPA
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Agora estende JpaRepository e usa Long como tipo do ID
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByEmail(String email);
}