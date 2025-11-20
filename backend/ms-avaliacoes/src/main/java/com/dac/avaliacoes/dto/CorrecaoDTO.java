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
public class CorrecaoDTO {
    private Long id;
    private Long tentativaId;
    private Long instrutorId;
    private String feedback;
    private Double notaParcial;
    private String status;
    private LocalDateTime dataCriacao;
}