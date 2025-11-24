import { Component, inject } from '@angular/core'; // Adicionado inject
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Adicionado Router
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

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
  
  // Injeção do Router para navegação
  private router = inject(Router);

  // Adicionei IDs aos cursos para a navegação funcionar
  cursos = [
    {
      id: 1, // ID adicionado
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
      id: 2, // ID adicionado
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
      id: 3, // ID adicionado
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
    if (curso.id) {
      // Redireciona para /videoaulas/2 (por exemplo)
      this.router.navigate(['/videoaulas', curso.id]);
    } else {
      console.error('Erro: Curso selecionado não possui ID.');
    }
  }

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

  formatarStatus(status: string): string {
    switch(status) {
      case 'inscrito': return 'Inscrito';
      case 'em-andamento': return 'Em Andamento';
      case 'pausado': return 'Pausado';
      default: return status;
    }
  }
}