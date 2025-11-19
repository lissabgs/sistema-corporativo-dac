import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// Removemos MatToolbarModule e MatMenuModule daqui, se ainda estivessem
// MatToolbarModule, MatMenuModule, // <- Remova estas linhas se existirem
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-instrutor',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule
  ],
  template: `
    <div class="page-container">
      <h1>Bem-vindo, Instrutor!</h1>
      
      <p class="subtitle">Painel de gestão acadêmica</p>

      <div class="actions-grid">
        <mat-card class="action-card" (click)="gerenciarCursos()">
          <mat-icon color="primary">school</mat-icon>
          <h3>Gerenciar Cursos</h3>
          <p>Crie, edite e organize os cursos da plataforma.</p>
        </mat-card>

        <mat-card class="action-card">
          <mat-icon color="accent">analytics</mat-icon>
          <h3>Relatórios</h3>
          <p>Visualize o desempenho dos alunos.</p>
        </mat-card>
      </div>
    </div>
  `,
  styles: [
    `
      .page-container {
        padding: 24px;
      }
      
      h1 {
        font-size: 2rem;
        margin-bottom: 8px;
        color: #1f2937;
      }

      .subtitle {
        color: #6b7280;
        margin-bottom: 32px;
      }

      .actions-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 24px;
      }

      .action-card {
        padding: 24px;
        text-align: center;
        cursor: pointer;
        transition: transform 0.2s, box-shadow 0.2s;
      }

      .action-card:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      }

      .action-card mat-icon {
        font-size: 48px;
        width: 48px;
        height: 48px;
        margin-bottom: 16px;
      }

      .action-card h3 {
        margin: 0 0 8px 0;
        font-weight: 600;
      }
    `
  ]
})
export class DashboardInstrutorComponent {
  constructor(private router: Router) {}

  gerenciarCursos() {
    this.router.navigate(['/gerenciar-cursos']);
  }
}