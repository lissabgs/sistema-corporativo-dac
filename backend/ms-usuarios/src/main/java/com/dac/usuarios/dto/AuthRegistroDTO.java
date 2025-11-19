package com.dac.usuarios.dto;

import com.dac.usuarios.model.Perfil;

public class AuthRegistroDTO {

    private String email;
    private Perfil perfil;
    private Long usuarioId;
    private String senha;

    // Construtor com 4 argumentos (para correção)
    public AuthRegistroDTO(String email, Perfil perfil, Long usuarioId, String senha) {
        this.email = email;
        this.perfil = perfil;
        this.usuarioId = usuarioId;
        this.senha = senha;
    }

    // Certifique-se de que todos os getters/setters estejam aqui
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}