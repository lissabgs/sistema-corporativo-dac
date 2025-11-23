package com.dac.cursos.dto;

import java.util.List;

public class ModuloDTO {
    private String titulo;
    private int ordem;
    private List<AulaDTO> aulas;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public List<AulaDTO> getAulas() { return aulas; }
    public void setAulas(List<AulaDTO> aulas) { this.aulas = aulas; }
}