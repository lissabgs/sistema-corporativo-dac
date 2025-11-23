import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { FuncionarioService } from '../../../services/funcionario.service';
import { UsuarioFormDialogComponent } from './usuario-form-dialog/usuario-form-dialog';

@Component({
  selector: 'app-gerenciar-usuarios',
  standalone: true,
  imports: [
    CommonModule, MatTableModule, MatButtonModule, MatIconModule,
    MatDialogModule, MatSnackBarModule, MatChipsModule
  ],
  templateUrl: './gerenciar-usuarios.html',
  styleUrls: ['./gerenciar-usuarios.css']
})
export class GerenciarUsuariosComponent implements OnInit {
  displayedColumns: string[] = ['nome', 'email', 'cargo', 'departamento', 'perfil', 'acoes'];
  dataSource: any[] = [];

  constructor(
    private funcionarioService: FuncionarioService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados() {
    this.funcionarioService.listarFuncionarios().subscribe({
      next: (res) => this.dataSource = res,
      error: () => this.aviso('Erro ao carregar usuários')
    });
  }

  abrirDialog(usuario?: any) {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '600px',
      data: usuario
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const operacao = usuario 
          ? this.funcionarioService.atualizarFuncionario(usuario.id, result)
          : this.funcionarioService.criarFuncionario(result);

        operacao.subscribe({
          next: () => {
            this.aviso(usuario ? 'Atualizado com sucesso!' : 'Criado com sucesso!');
            this.carregarDados();
          },
          error: () => this.aviso('Erro na operação.')
        });
      }
    });
  }

  inativar(id: number) {
    if(confirm('Inativar este usuário?')) {
      this.funcionarioService.inativarFuncionario(id).subscribe({
        next: () => { this.aviso('Usuário inativado.'); this.carregarDados(); },
        error: () => this.aviso('Erro ao inativar.')
      });
    }
  }

  aviso(msg: string) {
    this.snackBar.open(msg, 'Fechar', { duration: 3000 });
  }
}