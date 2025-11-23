import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';  //  <<=== AQUI

// Importe o Service e o Modelo
import { CursoService } from '../../../services/curso.service';
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-gerenciar-cursos',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './gerenciar-cursos.component.html',
  styleUrls: ['./gerenciar-cursos.component.css']
})
export class GerenciarCursosComponent implements OnInit {
  
  // Usamos a interface Curso real definida no models
  cursos: Curso[] = []; 
  loading = true;

  constructor(
    private router: Router,
    private cursoService: CursoService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarCursos();
  }

  carregarCursos() {
    // 1. Recupera o ID do instrutor logado
    const usuarioId = localStorage.getItem('usuarioId');

    if (usuarioId) {
      this.loading = true;
      // 2. Chama o serviço (GET /api/cursos/instrutor/{id})
      this.cursoService.listarPorInstrutor(+usuarioId).subscribe({
        next: (dados) => {
          this.cursos = dados;
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao buscar cursos:', err);
          this.snackBar.open('Erro ao carregar seus cursos.', 'Fechar', { duration: 3000 });
          this.loading = false;
        }
      });
    } else {
      this.router.navigate(['/login']);
    }
  }

  editarCurso(cursoId: number) {
    // Passa o ID via State para a página de cadastro preencher o form
    this.router.navigate(['/cadastrar-curso'], { state: { cursoId: cursoId } });
  }

  excluirCurso(cursoId: number) {
    // TODO: Implementar chamada real de delete no Service
    if (confirm('Tem certeza que deseja inativar este curso?')) {
      console.log(`Curso ${cursoId} seria excluído.`);
      // Mock visual de exclusão
      this.cursos = this.cursos.filter(c => c.id !== cursoId);
    }
  }

  cadastrarNovoCurso() {
    this.router.navigate(['/cadastrar-curso']);
  }
}