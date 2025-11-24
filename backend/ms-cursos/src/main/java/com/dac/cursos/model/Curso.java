package com.dac.cursos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String titulo;

    @Column(length = 1000)
    private String descricao;

    private String categoriaId;
    private Long instrutorId;

    @Transient
    private String instrutorNome;

    private String duracaoEstimada;

    private int xpOferecido;

    private int xpAvaliacao;
    private int xpConclusao;

    private String nivelDificuldade;

    @Enumerated(EnumType.STRING)
    private StatusCurso status;

    @ElementCollection
    @CollectionTable(name = "curso_pre_requisitos", joinColumns = @JoinColumn(name = "curso_id"))
    @Column(name = "pre_requisito_codigo")
    private List<String> preRequisitos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private List<Modulo> modulos = new ArrayList<>();
}