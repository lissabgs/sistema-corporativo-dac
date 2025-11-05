package com.dac.usuarios.dto;

import com.dac.usuarios.model.Perfil;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioUpdateDTO {
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    private String cargo;
    private Long departamentoId;
    private Perfil perfil;
    private Boolean status;
}