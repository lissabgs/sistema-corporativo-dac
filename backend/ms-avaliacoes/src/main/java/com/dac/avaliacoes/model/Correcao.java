package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "correcoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Correcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tentativa_id", nullable = false)
    private Tentativa tentativa;

    @Column(nullable = false)
    private Long instrutorId;              // ← FK soft (referencia ms-usuarios)

    @Column(columnDefinition = "TEXT")
    private String feedback;               // ← Comentário do instrutor

    @Column(nullable = false)
    private Double notaParcial;            // ← Nota dada pelo instrutor

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCorrecao status = StatusCorrecao.PENDENTE;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}