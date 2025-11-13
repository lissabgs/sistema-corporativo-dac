package com.dac.autenticacao.model;

// Novas importações do JPA (Jakarta Persistence)
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// Importações do MongoDB removidas

@Entity // Anotação do JPA
@Table(name = "usuarios") // Nome da tabela no Postgres
@Getter
@Setter
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incremental
    private Long id; // ID agora é Long para Postgres

    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId; // ID do funcionário no 'ms-usuarios'

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 256)
    private String senhaHash;

    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private String tipoUsuario; // FUNCIONARIO, INSTRUTOR, ADMINISTRADOR

    @Column(nullable = false)
    private Boolean status = true;
}