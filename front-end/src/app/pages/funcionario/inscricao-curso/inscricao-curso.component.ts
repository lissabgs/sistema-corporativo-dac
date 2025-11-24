import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
// Removi MatChipsModule e MatListModule pois fiz com div/css puro para ter mais controle visual, mas pode manter se quiser.

@Component({
  selector: 'app-inscricao-curso',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule
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
      avaliacaoMedia: 4.8,
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
      avaliacaoMedia: 5.0,
      status: "pausado"
    }
  ];

  assistir(curso: any) {
    // Aqui você faria o redirecionamento, ex: this.router.navigate(['/player', curso.id]);
    console.log(`Iniciando player para o curso: ${curso.titulo}`);
    alert(`Redirecionando para a aula de ${curso.titulo}...`);
  }

  cancelar(curso: any) {
    const confirmar = confirm(`Deseja realmente cancelar sua inscrição no curso "${curso.titulo}"?`);
    if (confirmar) {
      // Lógica real de cancelamento
      this.cursos = this.cursos.filter(c => c !== curso);
    }
  }

  pausar(curso: any) {
    curso.status = 'pausado';
  }

  retomar(curso: any) {
    curso.status = 'em-andamento';
  }

  // Helper para deixar o texto do status bonito na tela
  formatarStatus(status: string): string {
    switch(status) {
      case 'inscrito': return 'Inscrito';
      case 'em-andamento': return 'Em Andamento';
      case 'pausado': return 'Pausado';
      default: return status;
    }
  }
}