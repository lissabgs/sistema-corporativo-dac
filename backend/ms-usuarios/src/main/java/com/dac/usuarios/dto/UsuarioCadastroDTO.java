package com.dac.usuarios.dto;

import com.dac.usuarios.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastroDTO {

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Cargo é obrigatório")
    private String cargo;

    @NotNull(message = "ID do Departamento é obrigatório")
    private Long departamentoId;

    @NotNull(message = "Perfil é obrigatório (ADMINISTRADOR ou INSTRUTOR)")
    private Perfil perfil;
}