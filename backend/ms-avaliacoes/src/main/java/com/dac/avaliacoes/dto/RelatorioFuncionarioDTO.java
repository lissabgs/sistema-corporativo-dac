package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFuncionarioDTO {
    private String funcionarioNome;
    private String departamento;
    private Integer totalAvaliacoes;
    private Integer totalAprovacoes;
    private Double mediaNotas;
    private Integer xpTotal;
    private String nivel;
}