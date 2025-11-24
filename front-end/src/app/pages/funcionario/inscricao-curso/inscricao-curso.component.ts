import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common'; // Importante para SSR
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
  
  // Injeta o identificador da plataforma para saber se estamos no navegador ou no servidor
  private platformId = inject(PLATFORM_ID);

  cursos: any[] = [];
  loading = true;

  ngOnInit(): void {
    // CORREÇÃO CRÍTICA: Só executa se estiver no navegador
    if (isPlatformBrowser(this.platformId)) {
      this.carregarMeusCursos();
    } else {
      this.loading = false; // Se for servidor, não carrega nada
    }
  }

  carregarMeusCursos() {
    // Agora é seguro usar localStorage
    const userId = localStorage.getItem('usuarioId');
    
    // Se não tiver ID (ex: não logado), usa 1 para teste ou redireciona
    const funcionarioId = userId ? +userId : 1; 

    console.log('Buscando cursos para o funcionário:', funcionarioId);

    this.progressoService.listarMeusCursos(funcionarioId).subscribe({
      next: (progressos) => {
        console.log('Progressos encontrados:', progressos);
        this.cursos = [];
        
        if (!progressos || progressos.length === 0) {
          this.loading = false;
          return;
        }

        // Para cada inscrição, busca os detalhes completos do curso
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
            error: (err) => console.error(`Erro ao carregar detalhe do curso ${progresso.cursoId}`, err)
          });
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro na requisição GET /progresso:', err);
        // Exibe erro visual se for 403 (Token inválido) ou 500 (Erro servidor)
        if (err.status === 403) {
            this.snackBar.open('Sessão expirada. Faça login novamente.', 'OK');
        } else {
            this.snackBar.open('Erro ao carregar cursos. Verifique se o servidor está rodando.', 'Fechar');
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
    return s.toLowerCase();
  }

  assistir(curso: any) {
    if (curso.id) {
      this.router.navigate(['/videoaulas', curso.id]);
    }
  }

  cancelar(curso: any) {
    if(confirm(`Cancelar inscrição em "${curso.titulo}"?`)) {
        // Futuramente: Chamar endpoint de delete
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