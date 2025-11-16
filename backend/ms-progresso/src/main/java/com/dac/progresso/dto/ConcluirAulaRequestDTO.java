package com.dac.progresso.dto;

public class ConcluirAulaRequestDTO {

    private Long funcionarioId;
    private String cursoId;
    private String aulaId; // ID da aula (ou a ordem/título)
    private int xpGanho; // XP que esta aula vale

    // Getters
    public Long getFuncionarioId() { return funcionarioId; }
    public String getCursoId() { return cursoId; }
    public String getAulaId() { return aulaId; }
    public int getXpGanho() { return xpGanho; }
}