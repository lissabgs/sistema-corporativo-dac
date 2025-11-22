package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaDepartamentoDTO {
    private String departamentoNome;
    private Integer totalFuncionarios;
    private Integer totalTentativas;
    private Double mediaNotas;
    private Double taxaAprovacao;
    private Integer totalXp;
}
