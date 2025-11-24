package com.dac.avaliacoes.dto;

import java.util.List;

public class CorrecaoRequestDTO {
    private Long tentativaId;
    private List<ItemCorrecaoDTO> itensCorrecao;

    // Getters e Setters
    public Long getTentativaId() { return tentativaId; }
    public void setTentativaId(Long tentativaId) { this.tentativaId = tentativaId; }

    public List<ItemCorrecaoDTO> getItensCorrecao() { return itensCorrecao; }
    public void setItensCorrecao(List<ItemCorrecaoDTO> itensCorrecao) { this.itensCorrecao = itensCorrecao; }

    // Classe interna estática para os itens da lista
    public static class ItemCorrecaoDTO {
        private Long respostaId;
        private Long questaoId;
        private Double notaAtribuida;
        private String comentarios;

        // Getters e Setters
        public Long getRespostaId() { return respostaId; }
        public void setRespostaId(Long respostaId) { this.respostaId = respostaId; }

        public Long getQuestaoId() { return questaoId; }
        public void setQuestaoId(Long questaoId) { this.questaoId = questaoId; }

        public Double getNotaAtribuida() { return notaAtribuida; }
        public void setNotaAtribuida(Double notaAtribuida) { this.notaAtribuida = notaAtribuida; }

        public String getComentarios() { return comentarios; }
        public void setComentarios(String comentarios) { this.comentarios = comentarios; }
    }
}