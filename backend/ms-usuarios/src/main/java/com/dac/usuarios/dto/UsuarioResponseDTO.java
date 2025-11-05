package com.dac.usuarios.dto;

import com.dac.usuarios.model.Funcionario;
import com.dac.usuarios.model.Perfil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioResponseDTO {
    private Long id;
    private String cpf;
    private String nome;
    private String email;
    private String cargo;
    private Integer xpTotal;
    private String nivel;
    private Boolean status;
    private LocalDate dataCadastro;
    private Perfil perfil;
    private String departamentoNome; // Nome do departamento
    private Long departamentoId;

    public UsuarioResponseDTO(Funcionario funcionario) {
        this.id = funcionario.getId();
        this.cpf = funcionario.getCpf(); // CPF já está limpo
        this.nome = funcionario.getNome();
        this.email = funcionario.getEmail();
        this.cargo = funcionario.getCargo();
        this.xpTotal = funcionario.getXpTotal();
        this.nivel = funcionario.getNivel();
        this.status = funcionario.getStatus();
        this.dataCadastro = funcionario.getDataCadastro();
        this.perfil = funcionario.getPerfil();
        if (funcionario.getDepartamento() != null) {
            this.departamentoNome = funcionario.getDepartamento().getNome();
            this.departamentoId = funcionario.getDepartamento().getId();
        }
    }
}