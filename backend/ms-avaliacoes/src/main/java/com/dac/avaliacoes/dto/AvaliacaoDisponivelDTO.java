package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDisponivelDTO {
    private Long avaliacaoId;
    private String codigo;
    private String titulo;
    private String descricao;
    private Integer tempoLimiteMinutos;
    private Integer tentativasRestantes;
    private Integer tentativasPermitidas;
    private Double notaMinima;
    private String cursoTitulo;
}
