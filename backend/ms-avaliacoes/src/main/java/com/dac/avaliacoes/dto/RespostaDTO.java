package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaDTO {
    private Long id;
    private Long tentativaId;
    private Long questaoId;
    private String respostaFuncionario;
    private Double pontuacao;
    private Boolean estaCorreta;
}