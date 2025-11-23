package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "correcoes")
public class Correcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tentativa_id", nullable = false)
    private Tentativa tentativa;

    @Column(nullable = false)
    private Long instrutorId;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(nullable = false)
    private Double notaParcial;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCorrecao status = StatusCorrecao.PENDENTE;

    // Construtor vazio
    public Correcao() {}

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tentativa getTentativa() { return tentativa; }
    public void setTentativa(Tentativa tentativa) { this.tentativa = tentativa; }

    public Long getInstrutorId() { return instrutorId; }
    public void setInstrutorId(Long instrutorId) { this.instrutorId = instrutorId; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Double getNotaParcial() { return notaParcial; }
    public void setNotaParcial(Double notaParcial) { this.notaParcial = notaParcial; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public StatusCorrecao getStatus() { return status; }
    public void setStatus(StatusCorrecao status) { this.status = status; }
}