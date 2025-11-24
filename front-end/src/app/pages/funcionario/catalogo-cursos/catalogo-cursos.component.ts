import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { CursoService } from '../../../services/curso.service';
import { ProgressoService } from '../../../services/progresso.service';
import { FuncionarioService } from '../../../services/funcionario.service';
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-matricula-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="dialog-container">
      <div class="dialog-header">
        <h2>Confirmar Inscrição</h2>
      </div>
      
      <mat-dialog-content class="dialog-content">
        <div class="icon-box">
          <mat-icon>school</mat-icon>
        </div>
        <p>Você deseja se matricular no curso:</p>
        <h3 class="curso-titulo">{{ data.titulo }}</h3>
        <p class="aviso">Ao confirmar, este curso aparecerá na sua lista de "Meus Cursos".</p>
      </mat-dialog-content>

      <mat-dialog-actions align="end" class="dialog-actions">
        <button mat-button mat-dialog-close>Cancelar</button>
        <button mat-raised-button color="primary" [mat-dialog-close]="true">
          <mat-icon>check_circle</mat-icon> Confirmar Matrícula
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-header { padding: 20px 24px; border-bottom: 1px solid #eee; background-color: #fff; }
    .dialog-header h2 { margin: 0; color: #0d47a1; font-size: 22px; }
    .dialog-content { padding: 30px 24px !important; text-align: center; display: flex; flex-direction: column; align-items: center; }
    .icon-box { width: 60px; height: 60px; background-color: #e3f2fd; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 15px; }
    .icon-box mat-icon { font-size: 32px; width: 32px; height: 32px; color: #0d47a1; }
    .curso-titulo { font-size: 18px; color: #333; margin: 10px 0; font-weight: 600; }
    .aviso { font-size: 12px; color: #777; margin-top: 5px; }
    .dialog-actions { padding: 15px 24px; border-top: 1px solid #eee; background-color: #fafafa; }
  `]
})
export class MatriculaDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<MatriculaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { titulo: string }
  ) {}
}

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
    MatDialogModule
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
    private dialog: MatDialog
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
          this.carregarCursosDisponiveis();
        },
        error: () => {
          this.snackBar.open('Erro ao carregar perfil.', 'Fechar');
          this.loading = false;
        }
      });
    }
  }

  carregarCursosDisponiveis() {
    if (!this.usuario) return;

    const depto = this.usuario.departamentoNome;
    const nivel = this.usuario.nivel || 'INICIANTE';
    const id = this.usuario.id; // ID para filtrar o que já tenho

    // Passamos o ID para o backend filtrar os cursos matriculados
    this.cursoService.listarDisponiveis(depto, nivel, id).subscribe({
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

  inscrever(curso: Curso) {
    if (!this.usuario?.id) return;

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

  realizarMatricula(curso: Curso) {
    this.progressoService.matricular(this.usuario.id, curso.codigo).subscribe({
      next: () => {
        this.snackBar.open('Matrícula realizada com sucesso!', 'Ir para Meus Cursos', { 
          duration: 5000
        }).onAction().subscribe(() => {
          this.router.navigate(['/meus-cursos']);
        });
        this.carregarCursosDisponiveis();
      },
      error: (err) => {
        console.error(err);
        if (err.status === 400) {
           this.snackBar.open('Não foi possível matricular. Verifique os dados.', 'Fechar');
        } else {
           this.snackBar.open('Erro ao realizar matrícula.', 'Fechar');
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