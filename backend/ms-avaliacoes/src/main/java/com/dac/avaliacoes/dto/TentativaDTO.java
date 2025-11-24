package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class TentativaDTO {
    private Long id;
    private Long funcionarioId;
    private Long avaliacaoId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double notaObtida;
    private String status;
    private Integer numeroTentativa;

    // --- CAMPOS QUE FALTAVAM ---
    private LocalDateTime dataTentativa;
    private String avaliacaoTitulo;

    // Construtor vazio
    public TentativaDTO() {}

    // Construtor completo (atualizado com os novos campos, opcional)
    public TentativaDTO(Long id, Long funcionarioId, Long avaliacaoId, LocalDateTime dataInicio,
                        LocalDateTime dataFim, Double notaObtida, String status, Integer numeroTentativa,
                        LocalDateTime dataTentativa, String avaliacaoTitulo) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.avaliacaoId = avaliacaoId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.notaObtida = notaObtida;
        this.status = status;
        this.numeroTentativa = numeroTentativa;
        this.dataTentativa = dataTentativa;
        this.avaliacaoTitulo = avaliacaoTitulo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public Long getAvaliacaoId() { return avaliacaoId; }
    public void setAvaliacaoId(Long avaliacaoId) { this.avaliacaoId = avaliacaoId; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Double getNotaObtida() { return notaObtida; }
    public void setNotaObtida(Double notaObtida) { this.notaObtida = notaObtida; }

    public void setNota(Double nota) {
        this.notaObtida = nota;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getNumeroTentativa() { return numeroTentativa; }
    public void setNumeroTentativa(Integer numeroTentativa) { this.numeroTentativa = numeroTentativa; }

    // --- GETTERS E SETTERS DOS NOVOS CAMPOS ---

    public LocalDateTime getDataTentativa() { return dataTentativa; }
    public void setDataTentativa(LocalDateTime dataTentativa) {
        this.dataTentativa = dataTentativa;
    }

    public String getAvaliacaoTitulo() { return avaliacaoTitulo; }
    public void setAvaliacaoTitulo(String avaliacaoTitulo) {
        this.avaliacaoTitulo = avaliacaoTitulo;
    }
}