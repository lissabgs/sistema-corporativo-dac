package com.dac.autenticacao.dto;

// import lombok.Getter; // Removido
// import lombok.Setter; // Removido

// @Getter // Removido
// @Setter // Removido
public class LoginRequestDTO {

    private String email;
    private String senha;

    // Getters e Setters manuais

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}