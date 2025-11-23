package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.dto.DashboardInstrutorDTO;
import com.dac.avaliacoes.service.DashboardInstrutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/instrutor")
public class DashboardInstrutorController {

    @Autowired
    private DashboardInstrutorService dashboardService;

    /**
     * R14: Dashboard do Instrutor
     * GET /api/dashboard/instrutor?instrutorId=1
     *
     * Retorna estatísticas consolidadas do instrutor:
     * - Total de avaliações criadas
     * - Total de tentativas realizadas
     * - Tentativas pendentes de correção
     * - Média geral de notas
     * - Avaliações recentes
     * - Lista de tentativas pendentes
     * - Estatísticas por avaliação
     *
     * Acesso: INSTRUTOR e ADMINISTRADOR
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMINISTRADOR')")
    public ResponseEntity<DashboardInstrutorDTO> getDashboard(
            @RequestParam(required = false) Long instrutorId) {

        // Se instrutorId não for fornecido, usar o ID padrão
        // Futuramente, pode extrair do token JWT
        if (instrutorId == null) {
            instrutorId = 1L; // Valor padrão para teste
        }

        DashboardInstrutorDTO dashboard = dashboardService.getDashboard(instrutorId);
        return ResponseEntity.ok(dashboard);
    }
}
