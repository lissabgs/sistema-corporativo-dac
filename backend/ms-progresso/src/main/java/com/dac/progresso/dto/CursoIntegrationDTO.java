package com.dac.progresso.dto;

import lombok.Data;
import java.util.List;

@Data
public class CursoIntegrationDTO {
    private Long id;
    private int xpConclusao;
    private List<ModuloDTO> modulos;

    @Data
    public static class ModuloDTO {
        private List<AulaDTO> aulas;
    }

    @Data
    public static class AulaDTO {
        private String id;
        private boolean obrigatorio;
    }
}