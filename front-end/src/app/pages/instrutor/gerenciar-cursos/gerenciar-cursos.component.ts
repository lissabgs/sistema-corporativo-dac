import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router'; // <--- Adicionado RouterModule
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// Importe o Service e o Modelo
import { CursoService } from '../../../services/curso.service';
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-gerenciar-cursos',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule, // <--- Importante para o routerLink funcionar
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
      // 2. Chama o serviço
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
    // Ajustei a rota para incluir '/instrutor' caso suas rotas filhas precisem
    this.router.navigate(['/instrutor/cadastrar-curso'], { state: { cursoId: cursoId } });
  }

  excluirCurso(cursoId: number) {
    if (confirm('Tem certeza que deseja inativar este curso?')) {
      // Mock visual de exclusão (aqui você chamaria o this.cursoService.delete(cursoId))
      console.log(`Curso ${cursoId} seria excluído.`);
      this.cursos = this.cursos.filter(c => c.id !== cursoId);
      this.snackBar.open('Curso inativado (simulação).', 'Fechar', { duration: 2000 });
    }
  }

  cadastrarNovoCurso() {
    this.router.navigate(['/instrutor/cadastrar-curso']);
  }
}