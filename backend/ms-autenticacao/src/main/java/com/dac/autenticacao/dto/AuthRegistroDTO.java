package com.dac.autenticacao.dto;
// Note que o package agora é 'com.dac.autenticacao.dto'

// Removemos a importação do 'Perfil' que não existe aqui

// (Não precisamos de @Getter e @Setter, vamos adicionar manualmente)
public class AuthRegistroDTO {

    private String email;
    private String perfil; // Mudamos de 'Object' ou 'Perfil' para 'String'
    private Long usuarioId;
    private String senha; // ADICIONADO CAMPO SENHA

    // Construtor vazio para o Jackson (Framework)
    public AuthRegistroDTO() {}

    // Construtor para inicialização (opcional)
    public AuthRegistroDTO(String email, String perfil, Long usuarioId, String senha) {
        this.email = email;
        this.perfil = perfil;
        this.usuarioId = usuarioId;
        this.senha = senha;
    }

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

    // NOVO GETTER (Para corrigir o erro de compilação)
    public String getSenha() {
        return senha;
    }

    // NOVO SETTER
    public void setSenha(String senha) {
        this.senha = senha;
    }
}