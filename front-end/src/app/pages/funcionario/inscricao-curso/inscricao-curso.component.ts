import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-inscricao-curso',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatListModule,
    MatDividerModule
  ],
  templateUrl: './inscricao-curso.component.html',
  styleUrls: ['./inscricao-curso.component.css']
})
export class InscricaoCursoComponent {

  cursos = [
    {
      titulo: "Angular Material Essentials",
      descricao: "Aprenda componentes do Angular Material na prática.",
      categoria: "Frontend",
      dificuldade: "Intermediário",
      xp: 120,
      duracao: 8,
      instrutor: "Maria Oliveira",
      avaliacaoMedia: null,
      status: "inscrito"
    },
    {
      titulo: "Java Orientado a Objetos",
      descricao: "Domine encapsulamento, herança e polimorfismo.",
      categoria: "Backend",
      dificuldade: "Avançado",
      xp: 160,
      duracao: 12,
      instrutor: "Carlos Mendes",
      avaliacaoMedia: null,
      status: "em-andamento"
    },
    {
      titulo: "Git & GitHub Avançado",
      descricao: "Fluxos, PRs, branches e automações.",
      categoria: "Dev Tools",
      dificuldade: "Intermediário",
      xp: 110,
      duracao: 6,
      instrutor: "Ana Paula",
      avaliacaoMedia: null,
      status: "pausado"
    }
  ];

  cancelar(curso: any) {
    const confirmar = confirm(`Deseja realmente cancelar sua inscrição no curso "${curso.titulo}"?`);

    if (confirmar) {
      this.cursos = this.cursos.filter(c => c !== curso);
    }
  }

  pausar(curso: any) {
    curso.status = 'pausado';
  }

  retomar(curso: any) {
    curso.status = 'em-andamento';
  }
}
