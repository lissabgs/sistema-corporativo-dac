package com.dac.gamificacao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "pontuacoes")
public class Pontuacao {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long funcionarioId;

    private int xpTotal;
    private String nivel;
    private Set<String> badges;

    public Pontuacao(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
        this.xpTotal = 0;
        this.nivel = "Iniciante";
        this.badges = new HashSet<>();
    }

    // Getters e Setters
    public String getId() { return id; }
    public Long getFuncionarioId() { return funcionarioId; }
    public int getXpTotal() { return xpTotal; }
    public String getNivel() { return nivel; }
    public Set<String> getBadges() { return badges; }

    public void setXpTotal(int xpTotal) {
        this.xpTotal = xpTotal;
        // (Aqui entraria a lógica futura para atualizar o 'nivel')
    }
}