import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog'; // <--- Adicionado MatDialogModule

import { MatriculaDialogComponent } from './matricula-dialog/matricula-dialog.component'; 

// Imports dos Services
import { CursoService } from '../../../services/curso.service';
import { ProgressoService } from '../../../services/progresso.service';
import { FuncionarioService } from '../../../services/funcionario.service';
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-catalogo-cursos',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatButtonModule, 
    MatIconModule, 
    MatChipsModule,
    MatSnackBarModule,
    MatDialogModule // <--- Importante para o dialog funcionar
  ],
  templateUrl: './catalogo-cursos.component.html',
  styleUrls: ['./catalogo-cursos.component.css']
})
export class CatalogoCursosComponent implements OnInit {
  cursosDisponiveis: Curso[] = [];
  usuario: any = null;
  loading = true;

  constructor(
    private cursoService: CursoService,
    private progressoService: ProgressoService,
    private funcionarioService: FuncionarioService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog // <--- Injeção do Dialog
  ) {}

  ngOnInit() {
    this.carregarDadosUsuario();
  }

  carregarDadosUsuario() {
    const userId = localStorage.getItem('usuarioId');
    if (userId) {
      this.funcionarioService.obterPorId(+userId).subscribe({
        next: (user) => {
          this.usuario = user;
          // Assim que tivermos o usuário (e seu depto/nível), chamamos a busca filtrada no back
          this.carregarCursosDisponiveis();
        },
        error: () => {
          this.snackBar.open('Erro ao carregar perfil. Faça login novamente.', 'Fechar');
          this.loading = false;
        }
      });
    }
  }

  carregarCursosDisponiveis() {
    if (!this.usuario) return;

    const depto = this.usuario.departamentoNome;
    const nivel = this.usuario.nivel || 'INICIANTE';

    this.cursoService.listarDisponiveis(depto, nivel).subscribe({
      next: (cursos) => {
        this.cursosDisponiveis = cursos; 
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Erro ao carregar catálogo.', 'Fechar');
        this.loading = false;
      }
    });
  }

  // --- MÉTODO DE INSCRIÇÃO COM MODAL ---
  inscrever(curso: Curso) {
    if (!this.usuario?.id) return;

    // Abre o Modal Bonito
    const dialogRef = this.dialog.open(MatriculaDialogComponent, {
      width: '400px',
      disableClose: true,
      data: { titulo: curso.titulo }
    });

    dialogRef.afterClosed().subscribe(confirmado => {
      if (confirmado) {
        this.realizarMatricula(curso);
      }
    });
  }

  // Separei a lógica da requisição
  realizarMatricula(curso: Curso) {
    this.progressoService.matricular(this.usuario.id, curso.codigo).subscribe({
      next: () => {
        this.snackBar.open('Matrícula realizada com sucesso!', 'Ir para Meus Cursos', { 
          duration: 5000,
          // panelClass: ['success-snackbar'] // Se tiver estilo global
        }).onAction().subscribe(() => {
          this.router.navigate(['/meus-cursos']);
        });
      },
      error: (err) => {
        console.error(err);
        // Tratamento de erro 400 (Bad Request) ou outros
        if (err.status === 400) {
           this.snackBar.open('Não foi possível matricular. Verifique se já está inscrito.', 'Fechar');
        } else {
           this.snackBar.open('Erro ao realizar matrícula. Tente novamente.', 'Fechar');
        }
      }
    });
  }

  getCorNivel(nivel: string): string {
    switch(nivel?.toUpperCase()) {
      case 'AVANCADO': return 'warn';
      case 'INTERMEDIARIO': return 'accent';
      default: return 'primary';
    }
  }
}