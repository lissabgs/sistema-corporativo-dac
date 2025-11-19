import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
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
export class AcessoNegadoComponent implements OnInit {
  usuarioPerfil: string = 'Desconhecido';

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    // Só acessa o localStorage se estiver no navegador
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioPerfil = localStorage.getItem('usuarioPerfil') || 'Desconhecido';
    }
  }

  voltarDashboard() {
    // Verifica plataforma também aqui para segurança, embora o click seja sempre no browser
    if (isPlatformBrowser(this.platformId)) {
      const perfil = localStorage.getItem('usuarioPerfil');
      
      if (perfil === 'FUNCIONARIO') {
        this.router.navigate(['/dashboard-funcionario']);
      } else if (perfil === 'INSTRUTOR' || perfil === 'ADMINISTRADOR') {
        this.router.navigate(['/dashboard-instrutor']);
      } else {
        this.router.navigate(['/login']);
      }
    }
  }
}