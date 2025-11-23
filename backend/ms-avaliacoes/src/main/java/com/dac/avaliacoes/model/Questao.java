package com.dac.avaliacoes.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questoes")
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoQuestao tipoQuestao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(columnDefinition = "TEXT")
    private String opcoesResposta;

    private String respostaCorreta;

    @Column(nullable = false)
    private Double peso = 1.0;

    @Column(nullable = false)
    private Integer ordem;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    // Construtor vazio
    public Questao() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Avaliacao getAvaliacao() { return avaliacao; }
    public void setAvaliacao(Avaliacao avaliacao) { this.avaliacao = avaliacao; }

    public TipoQuestao getTipoQuestao() { return tipoQuestao; }
    public void setTipoQuestao(TipoQuestao tipoQuestao) { this.tipoQuestao = tipoQuestao; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getOpcoesResposta() { return opcoesResposta; }
    public void setOpcoesResposta(String opcoesResposta) { this.opcoesResposta = opcoesResposta; }

    public String getRespostaCorreta() { return respostaCorreta; }
    public void setRespostaCorreta(String respostaCorreta) { this.respostaCorreta = respostaCorreta; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public List<Resposta> getRespostas() { return respostas; }
    public void setRespostas(List<Resposta> respostas) { this.respostas = respostas; }
}