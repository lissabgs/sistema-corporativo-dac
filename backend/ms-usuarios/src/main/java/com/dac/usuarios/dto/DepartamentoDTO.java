package com.dac.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartamentoDTO {
    private String codigo;
    private String nome;
    private String descricao;
    private Long gestorId;
}