package com.dac.autenticacao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios") // Mudou de @Entity para @Document
public class AuthUser {

    @Id
    private String id; // MongoDB usa String (ObjectId) por padrão

    private Long usuarioId; // ID vindo do ms-usuarios (Postgres) para vínculo
    private String email;
    private String senhaHash;
    private String tipoUsuario;
    private Boolean status = true;

    // Construtor vazio
    public AuthUser() {}

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}