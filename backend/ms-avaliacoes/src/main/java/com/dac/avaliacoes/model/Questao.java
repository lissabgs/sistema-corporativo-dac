package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "questoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                             // ← Muitas questões para uma avaliação
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @Enumerated(EnumType.STRING)           // ← Salva tipo como STRING (MULTIPLA_ESCOLHA, etc)
    @Column(nullable = false)
    private TipoQuestao tipoQuestao;

    @Column(nullable = false, columnDefinition = "TEXT")  // ← TEXT para textos longos
    private String enunciado;

    @Column(columnDefinition = "TEXT")     // ← JSON com opções: ["A", "B", "C", "D"]
    private String opcoesResposta;

    private String respostaCorreta;        // ← A resposta certa (pode ser "A" ou "Verdadeiro" ou null se dissertativa)

    @Column(nullable = false)
    private Double peso = 1.0;             // ← Peso da questão (1.0 = 1 ponto, 2.0 = 2 pontos)

    @Column(nullable = false)
    private Integer ordem;                 // ← Ordem de exibição (1ª, 2ª, 3ª...)

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;
}