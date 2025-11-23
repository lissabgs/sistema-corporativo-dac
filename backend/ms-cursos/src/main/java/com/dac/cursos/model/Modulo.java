package com.dac.cursos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modulos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private int ordem;

    // Relacionamento OneToMany com Aula
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id") // Cria coluna FK na tabela 'aulas'
    private List<Aula> aulas = new ArrayList<>();

    public Modulo(String titulo, int ordem, List<Aula> aulas) {
        this.titulo = titulo;
        this.ordem = ordem;
        this.aulas = aulas;
    }
}