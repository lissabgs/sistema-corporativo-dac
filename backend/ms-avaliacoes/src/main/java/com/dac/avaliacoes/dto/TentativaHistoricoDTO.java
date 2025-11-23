package com.dac.avaliacoes.dto;

import java.time.LocalDateTime;

public class TentativaHistoricoDTO {
    private Long tentativaId;
    private String avaliacaoTitulo;
    private LocalDateTime dataRealizacao;
    private Double notaObtida;
    private Double notaMinima;
    private String status;
    private Integer numeroTentativa;

    // Construtor vazio
    public TentativaHistoricoDTO() {}

    // Construtor completo
    public TentativaHistoricoDTO(Long tentativaId, String avaliacaoTitulo, LocalDateTime dataRealizacao,
                                 Double notaObtida, Double notaMinima, String status, Integer numeroTentativa) {
        this.tentativaId = tentativaId;
        this.avaliacaoTitulo = avaliacaoTitulo;
        this.dataRealizacao = dataRealizacao;
        this.notaObtida = notaObtida;
        this.notaMinima = notaMinima;
        this.status = status;
        this.numeroTentativa = numeroTentativa;
    }

    // Getters e Setters
    public Long getTentativaId() { return tentativaId; }
    public void setTentativaId(Long tentativaId) { this.tentativaId = tentativaId; }

    public String getAvaliacaoTitulo() { return avaliacaoTitulo; }
    public void setAvaliacaoTitulo(String avaliacaoTitulo) { this.avaliacaoTitulo = avaliacaoTitulo; }

    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }

    public Double getNotaObtida() { return notaObtida; }
    public void setNotaObtida(Double notaObtida) { this.notaObtida = notaObtida; }

    public Double getNotaMinima() { return notaMinima; }
    public void setNotaMinima(Double notaMinima) { this.notaMinima = notaMinima; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getNumeroTentativa() { return numeroTentativa; }
    public void setNumeroTentativa(Integer numeroTentativa) { this.numeroTentativa = numeroTentativa; }
}
