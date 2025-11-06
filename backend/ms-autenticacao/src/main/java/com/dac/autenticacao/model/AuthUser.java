package com.dac.autenticacao.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios") // Coleção no MongoDB
@Getter
@Setter
public class AuthUser {

    @Id
    private String id; // MongoDB usa String como ID

    private Long usuarioId; // ID do funcionário no banco 'ms-usuarios'

    private String email;

    private String senhaHash;

    private String tipoUsuario; // FUNCIONARIO, INSTRUTOR, ADMINISTRADOR

    private Boolean status = true;
}