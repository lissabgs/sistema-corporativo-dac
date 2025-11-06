package com.dac.autenticacao.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}