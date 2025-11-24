package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class TentativaPendenteDTO {
    private Long tentativaId;
    private Long funcionarioId;
    private String funcionarioNome;
    private String avaliacaoTitulo;
    private LocalDateTime dataFim;
    private Double notaObtida;
    private String status; // <--- CAMPO NOVO

    // Construtor vazio
    public TentativaPendenteDTO() {}

    // Construtor completo (Agora com 7 argumentos)
    public TentativaPendenteDTO(Long tentativaId, Long funcionarioId, String funcionarioNome,
                                String avaliacaoTitulo, LocalDateTime dataFim, Double notaObtida, String status) {
        this.tentativaId = tentativaId;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.avaliacaoTitulo = avaliacaoTitulo;
        this.dataFim = dataFim;
        this.notaObtida = notaObtida;
        this.status = status; // <--- Atribuição nova
    }

    // Getters e Setters
    public Long getTentativaId() { return tentativaId; }
    public void setTentativaId(Long tentativaId) { this.tentativaId = tentativaId; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getFuncionarioNome() { return funcionarioNome; }
    public void setFuncionarioNome(String funcionarioNome) { this.funcionarioNome = funcionarioNome; }

    public String getAvaliacaoTitulo() { return avaliacaoTitulo; }
    public void setAvaliacaoTitulo(String avaliacaoTitulo) { this.avaliacaoTitulo = avaliacaoTitulo; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Double getNotaObtida() { return notaObtida; }
    public void setNotaObtida(Double notaObtida) { this.notaObtida = notaObtida; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}