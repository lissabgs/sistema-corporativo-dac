
package com.dac.avaliacoes.controller;

import com.dac.avaliacoes.dto.DashboardFuncionarioDTO;
import com.dac.avaliacoes.service.DashboardFuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/funcionario")
public class DashboardFuncionarioController {

    @Autowired
    private DashboardFuncionarioService dashboardService;

    /**
     * R15: Dashboard do Funcionário
     * GET /api/dashboard/funcionario/{funcionarioId}
     */

    @GetMapping("/{funcionarioId}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<DashboardFuncionarioDTO> getDashboard(@PathVariable Long funcionarioId) {
        DashboardFuncionarioDTO dashboard = dashboardService.getDashboard(funcionarioId);
        return ResponseEntity.ok(dashboard);
    }
}
