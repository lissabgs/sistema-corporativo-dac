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

  abrirDialog(avaliacaoResumida?: Avaliacao) {
    
    if (avaliacaoResumida && avaliacaoResumida.id) {
      this.avaliacaoService.buscarPorId(avaliacaoResumida.id).subscribe({
        next: (avaliacaoCompleta) => {
          this.abrirModal(avaliacaoCompleta);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao carregar detalhes da avaliação.', 'Fechar');
        }
      });
    } else {
      this.abrirModal(undefined);
    }
  }

  // Método auxiliar para abrir o modal
  private abrirModal(dados?: Avaliacao) {
    const dialogRef = this.dialog.open(AvaliacaoFormDialogComponent, {
      width: '1000px',
      maxWidth: '95vw',
      disableClose: true, // Evita fechar clicando fora
      data: { avaliacao: dados }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarAvaliacoes();
        this.snackBar.open('Avaliação salva com sucesso!', 'OK', { duration: 3000 });
      }
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