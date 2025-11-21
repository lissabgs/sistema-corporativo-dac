package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestaoDTO {
    private Long id;
    private String tipoQuestao;
    private String enunciado;
    private String opcoesResposta;
    private String respostaCorreta;
    private Double peso;
    private Integer ordem;
}