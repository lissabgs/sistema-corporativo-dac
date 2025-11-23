package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class CorrecaoDTO {
    private Long id;
    private Long tentativaId;
    private Long instrutorId;
    private String feedback;
    private Double notaParcial;
    private String status;
    private LocalDateTime dataCriacao;

    // Construtor vazio
    public CorrecaoDTO() {}

    // Construtor completo
    public CorrecaoDTO(Long id, Long tentativaId, Long instrutorId, String feedback,
                       Double notaParcial, String status, LocalDateTime dataCriacao) {
        this.id = id;
        this.tentativaId = tentativaId;
        this.instrutorId = instrutorId;
        this.feedback = feedback;
        this.notaParcial = notaParcial;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTentativaId() { return tentativaId; }
    public void setTentativaId(Long tentativaId) { this.tentativaId = tentativaId; }

    public Long getInstrutorId() { return instrutorId; }
    public void setInstrutorId(Long instrutorId) { this.instrutorId = instrutorId; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Double getNotaParcial() { return notaParcial; }
    public void setNotaParcial(Double notaParcial) { this.notaParcial = notaParcial; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}