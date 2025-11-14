package com.dac.autenticacao.dto;

// Não precisamos de Lombok
public class LoginResponseDTO {

    private String token;
    private Long usuarioId;
    private String perfil;

    // Construtor que o AuthService usa
    public LoginResponseDTO(String token, Long usuarioId, String perfil) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.perfil = perfil;
    }

    // --- Getters Manuais ---
    // (Necessários para o Spring converter para JSON)

    public String getToken() {
        return token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getPerfil() {
        return perfil;
    }
}