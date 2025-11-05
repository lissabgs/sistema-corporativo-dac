package com.dac.usuarios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends EntidadeBase {

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String cargo;

    private Integer xpTotal;

    private String nivel;

    private LocalDate dataCadastro;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @PrePersist
    public void prePersist() {
        if (getXpTotal() == null) setXpTotal(0);
        if (getNivel() == null) setNivel("Iniciante");
        if (getStatus() == null) setStatus(true); // Status inicial ATIVO
        if (getDataCadastro() == null) setDataCadastro(LocalDate.now());
    }
}