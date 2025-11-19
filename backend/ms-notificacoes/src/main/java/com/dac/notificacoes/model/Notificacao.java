package com.dac.notificacoes.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;   // <--- IMPORT NOVO
import lombok.Data;                 // <--- IMPORT NOVO
import lombok.Getter;
import lombok.NoArgsConstructor;    // <--- IMPORT NOVO
import lombok.Setter;

@Data                 // Cria Getters, Setters, toString, etc. automaticamente
@NoArgsConstructor    // Cria o construtor vazio
@AllArgsConstructor   // Cria o construtor com todos os campos
@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;
    
    private Long usuarioId;
    private String titulo;
    private String mensagem;
    private String tipo;
    private String canal;
    private LocalDateTime dataCriacao;
    private boolean lida;

    // NÃO PRECISA MAIS ESCREVER GETTERS E SETTERS AQUI! O LOMBOK JÁ FEZ.
    
    // Dica: Se quiser manter aquele construtor personalizado que não recebe ID nem Data, 
    // você pode deixar ele aqui manualmente, sem problemas.
    public Notificacao(Long usuarioId, String titulo, String mensagem, String tipo, String canal) {
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.canal = canal;
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
    }
}