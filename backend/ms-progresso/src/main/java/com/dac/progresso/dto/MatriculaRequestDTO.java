package com.dac.progresso.dto;

public class MatriculaRequestDTO {
    private Long funcionarioId;
    private String cursoId; // Código do curso (ex: JAVA-BASIC)

    public MatriculaRequestDTO() {}

    public MatriculaRequestDTO(Long funcionarioId, String cursoId) {
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
    }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }
}