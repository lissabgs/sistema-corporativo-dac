import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-inscricao-curso',
  standalone: true,
  templateUrl: './inscricao-curso.component.html',
  styleUrls: ['./inscricao-curso.component.css'],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
})
export class InscricaoCursoComponent {

  curso: any;
  atendeRequisitos = true;
  inscrito = false;

  constructor(private route: ActivatedRoute, private router: Router) {
    // Receber dados do curso por parametro ou mock
    this.curso = history.state.curso || {
      id: 1,
      titulo: "Introdução ao Angular",
      descricao: "Aprenda os fundamentos do Angular.",
      duracao: 8,
      instrutor: "Maria Oliveira",
      dificuldade: "Iniciante",
      xp: 120,
      prerequisitosAtendidos: false,
      trilhaSugerida: [
        "Lógica de Programação",
        "Fundamentos de JavaScript"
      ]
    };

    this.atendeRequisitos = this.curso.prerequisitosAtendidos;
  }

  inscrever() {
    this.inscrito = true;

    const data = new Date().toISOString().split("T")[0];

    console.log("Status: EM_ANDAMENTO");
    console.log("Data de início:", data);

    setTimeout(() => {
      this.router.navigate(['/catalogo-cursos']);
    }, 2000);
  }
}
