package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarAvaliacaoRequestDTO {
    private String codigo;
    private String titulo;
    private String descricao;
    private Long cursoId;
    private Integer tempoLimiteMinutos;
    private Integer tentativasPermitidas;
    private Double notaMinima;
    private List<QuestaoDTO> questoes;
}
