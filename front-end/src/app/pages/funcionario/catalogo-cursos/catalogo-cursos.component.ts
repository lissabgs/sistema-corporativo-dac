import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

// Angular Material
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { CursoService } from '../../../services/curso.service';
import { ProgressoService } from '../../../services/progresso.service'; // <--- NOVO
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-catalogo-cursos',
  standalone: true,
  templateUrl: './catalogo-cursos.component.html',
  styleUrls: ['./catalogo-cursos.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatSnackBarModule
  ]
})
export class CatalogoCursosComponent implements OnInit {

  cursos: Curso[] = [];
  cursosFiltrados: Curso[] = [];
  
  // Armazena os IDs dos cursos que o aluno já faz
  inscricoesIds: Set<number> = new Set(); 

  // Filtros
  search = '';
  categoria = '';
  instrutor = '';
  dificuldade = '';
  avaliacaoMin = 0;

  categorias = ['Tecnologia', 'Backend', 'Soft Skills', 'Liderança'];
  instrutores = ['Maria Oliveira', 'Carlos Mendes', 'João Silva'];
  dificuldades = ['Iniciante', 'Intermediário', 'Avançado'];

  constructor(
    private cursoService: CursoService,
    private progressoService: ProgressoService, // <--- INJETADO
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados() {
    const usuarioId = Number(localStorage.getItem('usuarioId'));

    // 1. Carrega todas as inscrições do aluno
    this.progressoService.listarInscricoes(usuarioId).subscribe({
      next: (progressos) => {
        // Cria um Set com os IDs dos cursos inscritos para busca rápida
        this.inscricoesIds = new Set(progressos.map(p => Number(p.cursoId)));
        console.log('Cursos já inscritos (IDs):', this.inscricoesIds);
        
        // 2. Só depois carrega o catálogo (para já renderizar com o botão certo)
        this.carregarCursosDoCatalogo();
      },
      error: (err) => {
        console.error('Erro ao carregar inscrições', err);
        // Mesmo com erro, tenta carregar o catálogo
        this.carregarCursosDoCatalogo();
      }
    });
  }

  carregarCursosDoCatalogo() {
    this.cursoService.listarCursos().subscribe({
      next: (dados: Curso[]) => { // <--- ADICIONADO TIPO EXPLÍCITO
        this.cursos = dados;
        this.filtrar(); 
      },
      error: (err: any) => { // <--- ADICIONADO TIPO EXPLÍCITO
        this.snackBar.open('Erro ao carregar cursos.', 'Fechar');
      }
    });
  }

  // Verifica se o curso atual está na lista de inscritos
  jaInscrito(cursoId: number | undefined): boolean {
    if (!cursoId) return false;
    return this.inscricoesIds.has(cursoId);
  }

  // Ação do botão (inteligente)
  tratarAcao(curso: any) {
    if (this.jaInscrito(curso.id)) {
      // Se já inscrito, vai para o Dashboard (ou player do curso futuramente)
      this.snackBar.open('Você já está estudando este curso!', 'Ir para Aula', { duration: 3000 })
        .onAction().subscribe(() => this.router.navigate(['/dashboard-funcionario']));
    } else {
      // Se não, vai para detalhes/inscrição
      this.router.navigate(['/inscricao-curso'], { state: { curso: curso } });
    }
  }

  // ... (métodos filtrar e limparFiltros mantidos iguais ao anterior) ...
  filtrar() {
    this.cursosFiltrados = this.cursos.filter(c => {
      const matchSearch = this.search === '' || c.titulo.toLowerCase().includes(this.search.toLowerCase());
      const matchDif = this.dificuldade === '' || c.nivelDificuldade === this.dificuldade;
      return matchSearch && matchDif;
    });
  }

  limparFiltros() {
    this.search = '';
    this.categoria = '';
    this.instrutor = '';
    this.dificuldade = '';
    this.avaliacaoMin = 0;
    this.cursosFiltrados = [...this.cursos];
  }
}