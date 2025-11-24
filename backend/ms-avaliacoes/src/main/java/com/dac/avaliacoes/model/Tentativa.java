package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tentativas")
public class Tentativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long funcionarioId;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @Column(nullable = false)
    private Double notaObtida = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTentativa status = StatusTentativa.EM_PROGRESSO;

    @Column(nullable = false)
    private Integer numeroTentativa;

    @OneToMany(mappedBy = "tentativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    @OneToMany(mappedBy = "tentativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Correcao> correcoes;

    // Construtor vazio
    public Tentativa() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public Avaliacao getAvaliacao() { return avaliacao; }
    public void setAvaliacao(Avaliacao avaliacao) { this.avaliacao = avaliacao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Double getNotaObtida() { return notaObtida; }
    public void setNotaObtida(Double notaObtida) { this.notaObtida = notaObtida; }

    public StatusTentativa getStatus() { return status; }
    public void setStatus(StatusTentativa status) { this.status = status; }

    public Integer getNumeroTentativa() { return numeroTentativa; }
    public void setNumeroTentativa(Integer numeroTentativa) { this.numeroTentativa = numeroTentativa; }

    public List<Resposta> getRespostas() { return respostas; }
    public void setRespostas(List<Resposta> respostas) { this.respostas = respostas; }

    public List<Correcao> getCorrecoes() { return correcoes; }
    public void setCorrecoes(List<Correcao> correcoes) { this.correcoes = correcoes; }

    private Double notaFinal;

    public Double getNotaFinal() {
        return notaFinal;
    }
    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }
}