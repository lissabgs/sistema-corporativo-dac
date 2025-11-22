package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAvaliacaoDTO {
    private String avaliacaoCodigo;
    private String avaliacaoTitulo;
    private String cursoTitulo;
    private Integer totalRealizacoes;
    private Integer totalAprovados;
    private Integer totalReprovados;
    private Double mediaNotas;
    private Double taxaAprovacao;
}
