package com.dac.progresso.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime; // Importe isso
import java.util.HashSet;
import java.util.Set;

@Document(collection = "progresso")
public class Progresso {

    @Id
    private String id;
    private Long funcionarioId;
    private String cursoId;
    
    // --- NOVOS CAMPOS ---
    private StatusProgresso status;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    // --------------------

    private Set<String> aulasConcluidas;

    public Progresso(Long funcionarioId, String cursoId) {
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
        this.aulasConcluidas = new HashSet<>();
        
        // Valores padrão ao criar
        this.status = StatusProgresso.EM_ANDAMENTO;
        this.dataInicio = LocalDateTime.now();
    }

    // --- Adicione os Getters e Setters para os novos campos ---
    public String getId() { return id; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getCursoId() { return cursoId; }
    public Set<String> getAulasConcluidas() { return aulasConcluidas; }
    
    public StatusProgresso getStatus() { return status; }
    public void setStatus(StatusProgresso status) { this.status = status; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public void setAulasConcluidas(Set<String> aulasConcluidas) {
        this.aulasConcluidas = aulasConcluidas;
    }

    public boolean adicionarAula(String aulaId) {
        return this.aulasConcluidas.add(aulaId);
    }
}