import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <div class="page-container">
      <h1>Painel Administrativo</h1>
      <p class="subtitle">Gestão geral da plataforma corporativa</p>

      <div class="actions-grid">
        <mat-card class="action-card" (click)="navegar('/gerenciar-usuarios')">
          <mat-icon color="primary">people_alt</mat-icon>
          <h3>Gerenciar Usuários</h3>
          <p>Cadastrar, editar e inativar funcionários e instrutores.</p>
        </mat-card>

        <mat-card class="action-card" (click)="navegar('/gerenciar-departamentos')">
          <mat-icon color="accent">domain</mat-icon>
          <h3>Departamentos</h3>
          <p>Organizar estrutura corporativa e hierarquias.</p>
        </mat-card>

        <mat-card class="action-card">
          <mat-icon color="warn">bar_chart</mat-icon>
          <h3>Relatórios Globais</h3>
          <p>Visão geral de engajamento e performance da empresa.</p>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .page-container { padding: 24px; }
    h1 { font-size: 2rem; margin-bottom: 8px; color: #1f2937; font-family: 'Roboto', sans-serif; }
    .subtitle { color: #6b7280; margin-bottom: 32px; font-family: 'Roboto', sans-serif; }
    .actions-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px; }
    .action-card { padding: 24px; text-align: center; cursor: pointer; transition: transform 0.2s, box-shadow 0.2s; border-radius: 12px; }
    .action-card:hover { transform: translateY(-4px); box-shadow: 0 8px 16px rgba(0,0,0,0.1); }
    .action-card mat-icon { font-size: 48px; width: 48px; height: 48px; margin-bottom: 16px; }
    .action-card h3 { margin: 0 0 8px 0; font-weight: 600; font-size: 1.1rem; }
    .action-card p { color: #666; margin: 0; }
  `]
})
export class DashboardAdminComponent {
  constructor(private router: Router) {}

  navegar(rota: string) {
    this.router.navigate([rota]);
  }
}