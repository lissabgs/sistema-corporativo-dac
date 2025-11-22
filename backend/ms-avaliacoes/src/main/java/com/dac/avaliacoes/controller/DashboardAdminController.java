package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.dto.DashboardAdminDTO;
import com.dac.avaliacoes.dto.RelatorioGeralDTO;
import com.dac.avaliacoes.service.DashboardAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard/admin")
public class DashboardAdminController {

    @Autowired
    private DashboardAdminService dashboardService;

    /**
     * R16: Dashboard Administrativo
     * GET /api/dashboard/admin
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<DashboardAdminDTO> getDashboard() {
        DashboardAdminDTO dashboard = dashboardService.getDashboard();
        return ResponseEntity.ok(dashboard);
    }

    /**
     * R17: Relatório Geral
     * GET /api/dashboard/admin/relatorio?dataInicio=2025-01-01T00:00:00&dataFim=2025-01-31T23:59:59
     */
    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RelatorioGeralDTO> gerarRelatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        RelatorioGeralDTO relatorio = dashboardService.gerarRelatorioGeral(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
