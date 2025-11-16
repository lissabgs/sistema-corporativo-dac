package com.dac.usuarios.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Departamento extends EntidadeBase {

    private String codigo;
    private String nome;
    private String descricao;
    private Long gestorId;
}