package com.dac.usuarios.dto;

import com.dac.usuarios.model.Perfil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegistroDTO {
    private String email;
    private String senha;
    private Perfil perfil;
    private Long usuarioId; // ID do funcionário no banco do ms-usuarios

    public AuthRegistroDTO(String email, String senha, Perfil perfil, Long usuarioId) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.usuarioId = usuarioId;
    }
}