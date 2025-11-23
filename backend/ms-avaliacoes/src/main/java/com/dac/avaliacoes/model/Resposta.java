package com.dac.avaliacoes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respostas")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tentativa_id", nullable = false)
    private Tentativa tentativa;

    @ManyToOne
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    @Column(columnDefinition = "TEXT")
    private String respostaFuncionario;

    @Column(nullable = false)
    private Double pontuacao = 0.0;

    private Boolean estaCorreta;

    // Construtor vazio
    public Resposta() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tentativa getTentativa() { return tentativa; }
    public void setTentativa(Tentativa tentativa) { this.tentativa = tentativa; }

    public Questao getQuestao() { return questao; }
    public void setQuestao(Questao questao) { this.questao = questao; }

    public String getRespostaFuncionario() { return respostaFuncionario; }
    public void setRespostaFuncionario(String respostaFuncionario) { this.respostaFuncionario = respostaFuncionario; }

    public Double getPontuacao() { return pontuacao; }
    public void setPontuacao(Double pontuacao) { this.pontuacao = pontuacao; }

    public Boolean getEstaCorreta() { return estaCorreta; }
    public void setEstaCorreta(Boolean estaCorreta) { this.estaCorreta = estaCorreta; }
}