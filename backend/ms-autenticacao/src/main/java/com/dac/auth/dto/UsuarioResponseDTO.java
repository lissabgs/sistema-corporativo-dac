package com.dac.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private String id;
    private String cpf;
    private String nome;
    private String email;
    private String departamento;
    private String cargo;
    private String tipoUsuario;
    
    @Builder.Default
    private Boolean ativo = true;
    
    @Builder.Default
    private Integer xpTotal = 0;
    
    @Builder.Default
    private String nivel = "INICIANTE";
    
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimoAcesso;
}