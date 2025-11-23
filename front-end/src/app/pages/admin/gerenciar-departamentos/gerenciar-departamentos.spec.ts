import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DepartamentoService } from '../../../services/departamento.service';
// Importa o modal externo que criamos
import { DepartamentoFormDialogComponent } from './departamento-form-dialog/departamento-form-dialog';

@Component({
  selector: 'app-gerenciar-departamentos',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatDialogModule, 
    MatSnackBarModule, 
    MatTooltipModule // Necessário para o matTooltip do HTML
  ],
  // Aponta para os arquivos externos
  templateUrl: './gerenciar-departamentos.html',
  styleUrls: ['./gerenciar-departamentos.css']
})
export class GerenciarDepartamentosComponent implements OnInit {
  departamentos: any[] = [];
  displayedColumns: string[] = ['codigo', 'nome', 'descricao', 'acoes'];

  constructor(
    private service: DepartamentoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.carregar();
  }

  carregar() {
    this.service.listarDepartamentos().subscribe({
      next: (data) => this.departamentos = data,
      error: (err) => {
        console.error(err);
        this.snackBar.open('Erro ao carregar departamentos', 'Fechar');
      }
    });
  }

  // Abre o Modal importado
  abrirDialog(departamento?: any) {
    const dialogRef = this.dialog.open(DepartamentoFormDialogComponent, {
      width: '600px',
      maxWidth: '95vw',
      disableClose: true,
      data: { departamento: departamento }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregar(); // Recarrega a lista após salvar
        this.snackBar.open('Salvo com sucesso!', 'OK', { duration: 3000 });
      }
    });
  }

  // O método que estava faltando para o botão "Excluir"
  deletar(id: number) {
    if(confirm('Tem certeza que deseja remover este departamento?')) {
      this.service.deletar(id).subscribe({
        next: () => {
          this.snackBar.open('Removido com sucesso!', 'OK', { duration: 3000 });
          this.carregar();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao remover.', 'Fechar');
        }
      });
    }
  }
}