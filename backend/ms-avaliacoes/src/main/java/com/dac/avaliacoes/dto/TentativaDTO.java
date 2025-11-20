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
public class TentativaDTO {
    private Long id;
    private Long funcionarioId;
    private Long avaliacaoId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double notaObtida;
    private String status;
    private Integer numeroTentativa;
}
