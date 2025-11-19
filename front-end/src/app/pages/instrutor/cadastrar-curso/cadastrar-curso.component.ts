import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cadastrar-curso',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule
  ],
  template: `
    <mat-card style="max-width: 800px; margin: 30px auto; padding: 24px;">
      <h2>Formulário de Cadastro/Edição de Curso</h2>
      <p style="color: gray;">Esta tela será implementada futuramente.</p>
      <button mat-stroked-button (click)="voltar()">Voltar</button>
    </mat-card>
  `
})
export class CadastrarCursoComponent {
  constructor(private router: Router) {}

  voltar() {
    this.router.navigate(['/gerenciar-cursos']);
  }
}