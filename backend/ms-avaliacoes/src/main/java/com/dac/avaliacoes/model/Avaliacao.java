package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private Integer tempoLimiteMinutos;

    @Column(nullable = false)
    private Integer tentativasPermitidas;

    @Column(nullable = false)
    private Double notaMinima;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questao> questoes;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tentativa> tentativas;

    // Construtor vazio (obrigatório para JPA)
    public Avaliacao() {}

    // Métodos de ciclo de vida JPA
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Integer getTempoLimiteMinutos() { return tempoLimiteMinutos; }
    public void setTempoLimiteMinutos(Integer tempoLimiteMinutos) { this.tempoLimiteMinutos = tempoLimiteMinutos; }

    public Integer getTentativasPermitidas() { return tentativasPermitidas; }
    public void setTentativasPermitidas(Integer tentativasPermitidas) { this.tentativasPermitidas = tentativasPermitidas; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<Questao> getQuestoes() { return questoes; }
    public void setQuestoes(List<Questao> questoes) { this.questoes = questoes; }

    public List<Tentativa> getTentativas() { return tentativas; }
    public void setTentativas(List<Tentativa> tentativas) { this.tentativas = tentativas; }
}