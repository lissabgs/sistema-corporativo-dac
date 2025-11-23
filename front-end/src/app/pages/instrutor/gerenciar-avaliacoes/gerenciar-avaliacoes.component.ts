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

  // --- MÉTODO CORRIGIDO ---
  abrirDialog(avaliacaoResumida?: Avaliacao) {
    
    // 1. Se tem ID, é EDIÇÃO: Busca os dados completos no backend primeiro
    if (avaliacaoResumida && avaliacaoResumida.id) {
      this.avaliacaoService.buscarPorId(avaliacaoResumida.id).subscribe({
        next: (avaliacaoCompleta) => {
          // Agora sim, abrimos o modal com o objeto que tem as questões
          this.abrirModal(avaliacaoCompleta);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao carregar os dados da avaliação.', 'Fechar');
        }
      });
    } else {
      // 2. Se não tem ID, é CRIAÇÃO: Abre direto
      this.abrirModal(undefined);
    }
  }

  // Método auxiliar para abrir o modal (evita repetir código)
  private abrirModal(dados?: Avaliacao) {
    const dialogRef = this.dialog.open(AvaliacaoFormDialogComponent, {
      width: '1000px',
      maxWidth: '95vw',
      disableClose: true, // Importante: evita fechar clicando fora sem querer
      data: { avaliacao: dados }
    });

    dialogRef.afterClosed().subscribe(result => {
      // Se retornou true, é porque salvou com sucesso
      if (result) {
        this.carregarAvaliacoes();
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