import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DepartamentoService } from '../../../services/departamento.service';
// CORREÇÃO: Import sem o .component no final
import { DepartamentoFormDialogComponent } from './departamento-form-dialog/departamento-form-dialog';

@Component({
  selector: 'app-gerenciar-departamentos',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  // CORREÇÃO: Nomes dos arquivos sem .component
  templateUrl: './gerenciar-departamentos.html', 
  styleUrls: ['./gerenciar-departamentos.css']
})
export class GerenciarDepartamentosComponent implements OnInit {
  displayedColumns: string[] = ['codigo', 'nome', 'descricao', 'acoes'];
  dataSource: any[] = [];

  constructor(
    private departamentoService: DepartamentoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados() {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (res) => this.dataSource = res,
      error: () => this.mostrarMsg('Erro ao carregar departamentos')
    });
  }

  abrirDialog(departamento?: any) {
    const dialogRef = this.dialog.open(DepartamentoFormDialogComponent, {
      width: '400px',
      data: departamento
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (departamento) {
          this.departamentoService.atualizarDepartamento(departamento.id, result).subscribe({
            next: () => {
              this.mostrarMsg('Departamento atualizado!');
              this.carregarDados();
            },
            error: () => this.mostrarMsg('Erro ao atualizar.')
          });
        } else {
          this.departamentoService.criarDepartamento(result).subscribe({
            next: () => {
              this.mostrarMsg('Departamento criado!');
              this.carregarDados();
            },
            error: () => this.mostrarMsg('Erro ao criar.')
          });
        }
      }
    });
  }

  excluir(id: number) {
    if(confirm('Deseja realmente excluir este departamento?')) {
      this.departamentoService.deletarDepartamento(id).subscribe({
        next: () => {
          this.mostrarMsg('Departamento removido.');
          this.carregarDados();
        },
        error: () => this.mostrarMsg('Erro ao remover.')
      });
    }
  }

  mostrarMsg(msg: string) {
    this.snackBar.open(msg, 'Fechar', { duration: 3000 });
  }
}