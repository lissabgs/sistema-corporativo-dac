package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "respostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(columnDefinition = "TEXT")     // ← Resposta (texto, letra, etc)
    private String respostaFuncionario;

    @Column(nullable = false)
    private Double pontuacao = 0.0;        // ← Pontos ganhos (calculado automaticamente para objetivas)

    private Boolean estaCorreta;           // ← True/False/Null (para dissertativa)
}