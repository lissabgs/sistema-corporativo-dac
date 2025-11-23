package com.dac.cursos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private String urlConteudo;
    private int ordem;
    private boolean obrigatorio;
    private int xpModulo;

    // Construtor utilitário sem ID
    public Aula(String titulo, String descricao, String urlConteudo, int ordem, boolean obrigatorio, int xpModulo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.urlConteudo = urlConteudo;
        this.ordem = ordem;
        this.obrigatorio = obrigatorio;
        this.xpModulo = xpModulo;
    }
}