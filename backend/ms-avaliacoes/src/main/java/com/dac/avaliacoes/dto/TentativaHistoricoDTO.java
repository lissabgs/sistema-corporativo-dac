package com.dac.avaliacoes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TentativaHistoricoDTO {
    private Long tentativaId;
    private String avaliacaoTitulo;
    private LocalDateTime dataRealizacao;
    private Double notaObtida;
    private Double notaMinima;
    private String status; // APROVADO, REPROVADO, EM_CORRECAO
    private Integer numeroTentativa;
}
