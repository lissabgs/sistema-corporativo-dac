import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-dashboard-instrutor',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule
  ],
  // Usando template e styles inline para simplificar este componente
  template: `
    <mat-toolbar color="primary" class="toolbar">
      <span class="app-title">Dashboard Instrutor</span>
      <span class="spacer"></span>

      <button mat-button [matMenuTriggerFor]="opsMenu">
        Operações
        <mat-icon>expand_more</mat-icon>
      </button>
      <mat-menu #opsMenu="matMenu">
        <button mat-menu-item (click)="gerenciarCursos()">
          <mat-icon>school</mat-icon>
          <span>Gerenciar Cursos</span>
        </button>
      </mat-menu>

      <button mat-icon-button [matMenuTriggerFor]="userMenu" title="Opções de Usuário">
        <mat-icon>account_circle</mat-icon>
      </button>
      <mat-menu #userMenu="matMenu">
        <button mat-menu-item (click)="logout()">
          <mat-icon>logout</mat-icon>
          <span>Logout</span>
        </button>
      </mat-menu>
    </mat-toolbar>

    <div class="page" style="padding: 24px; min-height: calc(100vh - 64px); background-color: white;">
      <h1>Bem-vindo, Instrutor!</h1>
    </div>
  `,
  styles: [
    `
      .toolbar {
        position: sticky;
        top: 0;
        z-index: 100;
        background-color: #5c6bc0 !important;
        color: #fff;
      }
      .app-title {
        font-weight: 600;
        font-size: 1.3rem;
      }
      .spacer {
        flex: 1;
      }
    `
  ]
})
export class DashboardInstrutorComponent {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  gerenciarCursos() {
    this.router.navigate(['/gerenciar-cursos']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}