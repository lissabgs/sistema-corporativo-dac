package com.dac.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioAutocadastroDTO {
    private String cpf;
    private String nome;
    private String email;
    private String cargo;
    private Long departamentoId;
}