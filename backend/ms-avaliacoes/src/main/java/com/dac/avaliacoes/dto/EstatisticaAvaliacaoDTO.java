package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaAvaliacaoDTO {
    private Long avaliacaoId;
    private String avaliacaoTitulo;
    private Integer totalTentativas;
    private Integer tentativasAprovadas;
    private Integer tentativasReprovadas;
    private Double mediaNotas;
    private Double notaMinima;
    private Double notaMaxima;
    private Double taxaAprovacao; // percentual
}
