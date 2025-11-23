package com.dac.avaliacoes.dto;

public class QuestaoDTO {
    private Long id;
    private String tipoQuestao;
    private String enunciado;
    private String opcoesResposta;
    private String respostaCorreta;
    private Double peso;
    private Integer ordem;

    // Construtor vazio
    public QuestaoDTO() {}

    // Construtor completo
    public QuestaoDTO(Long id, String tipoQuestao, String enunciado, String opcoesResposta,
                      String respostaCorreta, Double peso, Integer ordem) {
        this.id = id;
        this.tipoQuestao = tipoQuestao;
        this.enunciado = enunciado;
        this.opcoesResposta = opcoesResposta;
        this.respostaCorreta = respostaCorreta;
        this.peso = peso;
        this.ordem = ordem;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoQuestao() { return tipoQuestao; }
    public void setTipoQuestao(String tipoQuestao) { this.tipoQuestao = tipoQuestao; }

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
}