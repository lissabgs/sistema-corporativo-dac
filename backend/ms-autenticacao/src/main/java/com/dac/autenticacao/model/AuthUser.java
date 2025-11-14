package com.dac.autenticacao.model;

import jakarta.persistence.*;
// import lombok.Getter; // Vamos remover a dependência do Lombok
// import lombok.Setter; // Vamos remover a dependência do Lombok

@Entity
@Table(name = "usuarios")
// @Getter // Removido
// @Setter // Removido
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 256)
    private String senhaHash;

    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private String tipoUsuario;

    @Column(nullable = false)
    private Boolean status = true;

    // --- Getters e Setters Manuais ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}