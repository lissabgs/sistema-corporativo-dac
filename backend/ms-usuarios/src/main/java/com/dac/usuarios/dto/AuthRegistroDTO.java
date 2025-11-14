package com.dac.usuarios.dto; // <-- 1. PACOTE CORRETO

import com.dac.usuarios.model.Perfil; // <-- 2. IMPORT CORRETO (para o Enum Perfil)
// (Não precisamos de Lombok, vamos usar getters/setters manuais)

public class AuthRegistroDTO {

    private String email;
    private Perfil perfil;
    private Long usuarioId;

    // Construtor que o FuncionarioService usa
    public AuthRegistroDTO(String email, Perfil perfil, Long usuarioId) {
        this.email = email;
        this.perfil = perfil;
        this.usuarioId = usuarioId;
    }

    // --- Getters e Setters Manuais ---

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}