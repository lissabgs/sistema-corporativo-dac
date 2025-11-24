import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

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
export class InscricaoCursoComponent implements OnInit {
  
  private router = inject(Router);
  private progressoService = inject(ProgressoService);
  private cursoService = inject(CursoService);
  private snackBar = inject(MatSnackBar);
  
  private platformId = inject(PLATFORM_ID);

  cursos: any[] = [];
  loading = true;

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.carregarMeusCursos();
    } else {
      this.loading = false;
    }
  }

  carregarMeusCursos() {
    const userId = localStorage.getItem('usuarioId');
    const funcionarioId = userId ? +userId : 1; 

    console.log('Buscando cursos para o funcionário:', funcionarioId);

    this.progressoService.listarMeusCursos(funcionarioId).subscribe({
      next: (progressos) => {
        this.cursos = [];
        
        if (!progressos || progressos.length === 0) {
          this.loading = false;
          return;
        }

        // Filtra cursos cancelados para não exibir na lista
        // (Caso o backend retorne tudo, filtramos aqui também por segurança)
        const progressosAtivos = progressos.filter((p: any) => p.status !== 'CANCELADO');

        progressosAtivos.forEach((progresso: any) => {
          this.cursoService.obterPorId(progresso.cursoId).subscribe({
            next: (detalhesCurso) => {
              this.cursos.push({
                ...detalhesCurso,
                status: this.tratarStatus(progresso.status),
                progressoId: progresso.id,
                aulasConcluidas: progresso.aulasConcluidas || 0
              });
            },
            error: (err) => console.error(`Erro ao carregar detalhe do curso ${progresso.cursoId}`, err)
          });
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro na requisição GET /progresso:', err);
        if (err.status === 403) {
            this.snackBar.open('Sessão expirada. Faça login novamente.', 'OK');
        } else {
            this.snackBar.open('Erro ao carregar cursos. Verifique o servidor.', 'Fechar');
        }
        this.loading = false;
      }
    });
  }

  tratarStatus(status: string): string {
    if (!status) return 'inscrito';
    const s = status.toUpperCase();
    if (s === 'EM_ANDAMENTO') return 'em-andamento';
    if (s === 'CONCLUIDO') return 'concluido';
    // Retorna 'pausado' ou 'cancelado' em minúsculo se vier do Java
    return s.toLowerCase();
  }

  // --- AÇÕES DO CURSO ---

  // Usado tanto para começar (de Inscrito) quanto para retomar (de Pausado)
  iniciar(curso: any) {
    if (!curso.progressoId) return;

    this.progressoService.iniciarCurso(curso.progressoId).subscribe({
      next: () => {
        curso.status = 'em-andamento';
        this.router.navigate(['/videoaulas', curso.id]);
      },
      error: (err) => {
        console.error('Erro ao iniciar curso:', err);
        // Tenta navegar mesmo com erro (fallback), ou exibe aviso
        this.router.navigate(['/videoaulas', curso.id]);
      }
    });
  }

  assistir(curso: any) {
    // Apenas navegação direta se já estiver em andamento
    if (curso.id) {
      this.router.navigate(['/videoaulas', curso.id]);
    }
  }

  retomar(curso: any) {
    this.iniciar(curso);
  }

  pausar(curso: any) {
    if (!curso.progressoId) return;

    this.progressoService.pausarCurso(curso.progressoId).subscribe({
      next: () => {
        curso.status = 'pausado';
        this.snackBar.open('Curso pausado.', 'Fechar', { duration: 2000 });
      },
      error: (err) => {
        console.error('Erro ao pausar:', err);
        this.snackBar.open('Erro ao pausar curso.', 'Fechar');
      }
    });
  }

  cancelar(curso: any) {
    const confirmacao = confirm(`Deseja cancelar a inscrição em "${curso.titulo}"?`);
    
    if (confirmacao && curso.progressoId) {
      this.progressoService.cancelarInscricao(curso.progressoId).subscribe({
        next: () => {
          // Remove da lista visualmente
          this.cursos = this.cursos.filter(c => c !== curso);
          this.snackBar.open('Inscrição cancelada.', 'OK', { duration: 3000 });
        },
        error: (err) => {
          console.error('Erro ao cancelar:', err);
          this.snackBar.open('Erro ao cancelar inscrição.', 'Fechar');
        }
      });
    }
  }

  formatarStatus(status: string): string {
    switch(status) {
      case 'inscrito': return 'Inscrito';
      case 'em-andamento': return 'Em Andamento';
      case 'pausado': return 'Pausado';
      case 'concluido': return 'Concluído';
      case 'cancelado': return 'Cancelado';
      default: return status;
    }
  }
}