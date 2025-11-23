package com.dac.avaliacoes.dto;

public class RespostaDTO {
    private Long id;
    private Long tentativaId;
    private Long questaoId;
    private String respostaFuncionario;
    private Double pontuacao;
    private Boolean estaCorreta;

    // Construtor vazio
    public RespostaDTO() {}

    // Construtor completo
    public RespostaDTO(Long id, Long tentativaId, Long questaoId, String respostaFuncionario,
                       Double pontuacao, Boolean estaCorreta) {
        this.id = id;
        this.tentativaId = tentativaId;
        this.questaoId = questaoId;
        this.respostaFuncionario = respostaFuncionario;
        this.pontuacao = pontuacao;
        this.estaCorreta = estaCorreta;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTentativaId() { return tentativaId; }
    public void setTentativaId(Long tentativaId) { this.tentativaId = tentativaId; }

    public Long getQuestaoId() { return questaoId; }
    public void setQuestaoId(Long questaoId) { this.questaoId = questaoId; }

    public String getRespostaFuncionario() { return respostaFuncionario; }
    public void setRespostaFuncionario(String respostaFuncionario) { this.respostaFuncionario = respostaFuncionario; }

    public Double getPontuacao() { return pontuacao; }
    public void setPontuacao(Double pontuacao) { this.pontuacao = pontuacao; }

    public Boolean getEstaCorreta() { return estaCorreta; }
    public void setEstaCorreta(Boolean estaCorreta) { this.estaCorreta = estaCorreta; }
}