import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

// Importe o serviço atualizado
import { AvaliacaoService } from '../../../services/avaliacao.service';

@Component({
  selector: 'app-listar-avaliacoes-curso',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './listar-avaliacoes-curso.component.html',
  styleUrls: ['./listar-avaliacoes-curso.component.css']
})
export class ListarAvaliacoesCursoComponent implements OnInit {
  cursoId!: number;
  tentativas: any[] = [];
  loading = true;
  
  // Colunas da tabela
  displayedColumns: string[] = ['aluno', 'avaliacao', 'dataEnvio', 'status', 'acoes'];

  constructor(
    private route: ActivatedRoute,
    private avaliacaoService: AvaliacaoService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    // Pega o ID da URL (ex: /curso/10/avaliacoes -> pega o 10)
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.cursoId = +idParam;
      this.carregarTentativas();
    }
  }

  carregarTentativas() {
    this.loading = true;
    this.avaliacaoService.buscarTentativasPorCurso(this.cursoId).subscribe({
      next: (dados) => {
        this.tentativas = dados;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar tentativas', err);
        this.snackBar.open('Erro ao carregar lista de avaliações.', 'Fechar', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}