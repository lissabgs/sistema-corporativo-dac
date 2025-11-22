package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tentativas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tentativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long funcionarioId;            // ← FK soft (referencia ms-usuarios)

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
    private Integer numeroTentativa;       // ← 1ª tentativa, 2ª tentativa, etc

    @OneToMany(mappedBy = "tentativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    @OneToMany(mappedBy = "tentativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Correcao> correcoes;


}