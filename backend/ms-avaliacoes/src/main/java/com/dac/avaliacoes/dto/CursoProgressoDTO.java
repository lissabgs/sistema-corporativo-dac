package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoProgressoDTO {
    private String cursoId;
    private String titulo;
    private String descricao;
    private Integer totalAulas;
    private Integer aulasConcluidas;
    private Double progressoPercentual;
    private Integer xpGanho;
    private Integer xpTotal;
}
