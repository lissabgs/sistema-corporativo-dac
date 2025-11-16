package com.dac.progresso.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "progresso")
public class Progresso {

    @Id
    private String id;
    private Long funcionarioId;
    private String cursoId;

    // Usamos um Set para garantir que não há aulas duplicadas
    private Set<String> aulasConcluidas;

    public Progresso(Long funcionarioId, String cursoId) {
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
        this.aulasConcluidas = new HashSet<>();
    }

    // Getters e Setters
    public String getId() { return id; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getCursoId() { return cursoId; }
    public Set<String> getAulasConcluidas() { return aulasConcluidas; }

    public void setAulasConcluidas(Set<String> aulasConcluidas) {
        this.aulasConcluidas = aulasConcluidas;
    }

    // Lógica para adicionar uma aula
    public boolean adicionarAula(String aulaId) {
        return this.aulasConcluidas.add(aulaId);
    }
}