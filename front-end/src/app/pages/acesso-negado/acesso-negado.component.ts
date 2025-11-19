import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-acesso-negado',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './acesso-negado.component.html',
  styleUrls: ['./acesso-negado.component.css']
})
export class AcessoNegadoComponent {
  constructor(private router: Router) {}

  /**
   * Redireciona para o dashboard apropriado com base no perfil do usuário logado.
   */
  voltarDashboard() {
    const perfil = localStorage.getItem('usuarioPerfil');
    
    if (perfil === 'FUNCIONARIO') {
      this.router.navigate(['/dashboard-funcionario']);
    } else if (perfil === 'INSTRUTOR') {
      this.router.navigate(['/dashboard-instrutor']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  // Permite acesso ao localStorage no template
  get localStorage() {
    return localStorage;
  }
}