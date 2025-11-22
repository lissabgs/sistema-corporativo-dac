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
public class TentativaPendenteDTO {
    private Long tentativaId;
    private Long funcionarioId;
    private String funcionarioNome;
    private String avaliacaoTitulo;
    private LocalDateTime dataFim;
    private Double notaObtida;
}
