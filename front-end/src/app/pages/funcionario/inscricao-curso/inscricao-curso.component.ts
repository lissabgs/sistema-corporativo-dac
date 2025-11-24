import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

// Importe os serviços
import { ProgressoService } from '../../../services/progresso.service';
import { CursoService } from '../../../services/curso.service';

@Component({
  selector: 'app-inscricao-curso',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './inscricao-curso.component.html',
  styleUrls: ['./inscricao-curso.component.css']
})
export class InscricaoCursoComponent implements OnInit { // <--- 1. Implementar OnInit
  
  private router = inject(Router);
  private progressoService = inject(ProgressoService); // <--- 2. Injetar serviços
  private cursoService = inject(CursoService);
  private snackBar = inject(MatSnackBar);

  cursos: any[] = []; // Começa vazio
  loading = true;

  // 3. Este método roda automaticamente ao abrir a página
  ngOnInit(): void {
    this.carregarMeusCursos();
  }

  carregarMeusCursos() {
    // Tenta pegar o ID do login (igual ao catálogo)
    const userId = localStorage.getItem('usuarioId');
    const funcionarioId = userId ? +userId : 1; // Se não tiver login, usa 1 para teste

    // Faz a requisição GET
    this.progressoService.listarMeusCursos(funcionarioId).subscribe({
      next: (progressos) => {
        this.cursos = [];
        
        if (progressos.length === 0) {
          this.loading = false;
          return;
        }

        // Para cada inscrição, busca os detalhes do curso
        progressos.forEach(progresso => {
          this.cursoService.obterPorId(progresso.cursoId).subscribe({
            next: (detalhesCurso) => {
              this.cursos.push({
                ...detalhesCurso,
                status: this.tratarStatus(progresso.status),
                progressoId: progresso.id,
                aulasConcluidas: progresso.aulasConcluidas || 0
              });
            },
            error: () => console.error(`Erro ao carregar detalhes do curso ${progresso.cursoId}`)
          });
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar cursos:', err);
        this.snackBar.open('Erro ao carregar seus cursos.', 'Fechar');
        this.loading = false;
      }
    });
  }

  tratarStatus(status: string): string {
    if (!status) return 'inscrito';
    const s = status.toUpperCase();
    if (s === 'EM_ANDAMENTO') return 'em-andamento';
    if (s === 'CONCLUIDO') return 'concluido';
    return s.toLowerCase();
  }

  // --- Mantenha os métodos de ação (assistir, cancelar...) ---

  assistir(curso: any) {
    if (curso.id) {
      this.router.navigate(['/videoaulas', curso.id]);
    }
  }

  cancelar(curso: any) {
    if(confirm(`Cancelar inscrição em "${curso.titulo}"?`)) {
        // Implementar lógica de cancelamento no backend aqui se necessário
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
      case 'concluido': return 'Concluído';
      default: return status;
    }
  }
}