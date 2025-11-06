package com.dac.autenticacao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegistroDTO {
    private String email;
    private String senha;
    private String perfil; // Recebemos como String
    private Long usuarioId;
}