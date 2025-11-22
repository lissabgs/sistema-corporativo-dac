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
public class AvaliacaoResumoDTO {
    private Long id;
    private String codigo;
    private String titulo;
    private Long cursoId;
    private Integer totalTentativas;
    private Double mediaNotas;
    private LocalDateTime dataCriacao;
}
