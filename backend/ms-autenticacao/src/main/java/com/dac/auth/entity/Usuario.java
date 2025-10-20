package com.dac.auth.entity;

import com.dac.auth.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    
    @Id
    private String id;
    
    private String cpf;
    private String nome;
    private String email;
    private String departamento;
    private String cargo;
    private String senha;
    
    @Builder.Default
    private TipoUsuario tipoUsuario = TipoUsuario.FUNCIONARIO;
    
    @Builder.Default
    private Boolean ativo = true;
    
    @Builder.Default
    private Integer xpTotal = 0;
    
    @Builder.Default
    private String nivel = "INICIANTE";
    
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    private LocalDateTime ultimoAcesso;
    private String ultimoIp;
}