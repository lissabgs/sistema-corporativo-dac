import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { CursoService } from '../../../services/curso.service';
import { Curso } from '../../../models/curso.model';

@Component({
  selector: 'app-acompanhar-turmas',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './acompanhar-turmas.component.html',
  styleUrls: ['./acompanhar-turmas.component.css']
})
export class AcompanharTurmasComponent implements OnInit {
  cursos: Curso[] = [];
  loading = true;

  constructor(
    private cursoService: CursoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const usuarioId = localStorage.getItem('usuarioId');
    if (usuarioId) {
      this.cursoService.listarPorInstrutor(+usuarioId).subscribe({
        next: (dados) => {
          this.cursos = dados;
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao carregar turmas.', 'Fechar', { duration: 3000 });
          this.loading = false;
        }
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
}