package com.dac.autenticacao.dto;
// Note que o package agora é 'com.dac.autenticacao.dto'

// Removemos a importação do 'Perfil' que não existe aqui

// (Não precisamos de @Getter e @Setter, vamos adicionar manualmente)
public class AuthRegistroDTO {

    private String email;
    private String perfil; // Mudamos de 'Object' ou 'Perfil' para 'String'
    private Long usuarioId;

    // Construtor vazio para o Jackson (Framework)
    public AuthRegistroDTO() {}

    // Getters e Setters manuais

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}