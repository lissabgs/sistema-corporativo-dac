import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AvaliacaoService } from '../../../services/avaliacao.service';
import { Avaliacao } from '../../../models/avaliacao.model';
import { AvaliacaoFormDialogComponent } from './avaliacao-form-dialog/avaliacao-form-dialog.component';

@Component({
  selector: 'app-gerenciar-avaliacoes',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  templateUrl: './gerenciar-avaliacoes.component.html',
  styleUrls: ['./gerenciar-avaliacoes.component.css']
})
export class GerenciarAvaliacoesComponent implements OnInit {
  avaliacoes: Avaliacao[] = [];
  displayedColumns: string[] = ['codigo', 'titulo', 'cursoId', 'tempo', 'acoes'];

  constructor(
    private avaliacaoService: AvaliacaoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarAvaliacoes();
  }

  carregarAvaliacoes() {
    this.avaliacaoService.listar().subscribe({
      next: (dados) => this.avaliacoes = dados,
      error: (err) => console.error(err)
    });
  }

  abrirDialog(avaliacao?: Avaliacao) {
    const dialogRef = this.dialog.open(AvaliacaoFormDialogComponent, {
      width: '1000px', // Aumentei para 1000px para caber tudo confortavelmente
      maxWidth: '95vw', // Garante que não ultrapasse a tela em celulares
      data: { avaliacao: avaliacao }
    });
  }

  deletar(id: number) {
    if(confirm('Tem certeza que deseja excluir esta avaliação?')) {
      this.avaliacaoService.deletar(id).subscribe({
        next: () => {
          this.snackBar.open('Excluído com sucesso', 'OK', { duration: 3000 });
          this.carregarAvaliacoes();
        },
        error: () => this.snackBar.open('Erro ao excluir', 'Fechar')
      });
    }
  }
}