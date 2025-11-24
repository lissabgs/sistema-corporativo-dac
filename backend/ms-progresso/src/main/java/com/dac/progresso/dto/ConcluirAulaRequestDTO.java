package com.dac.progresso.dto;

import lombok.Data;

@Data
public class ConcluirAulaRequestDTO {
    private Long funcionarioId;
    private String cursoId;
    private String aulaId;
    private int xpGanho;
}