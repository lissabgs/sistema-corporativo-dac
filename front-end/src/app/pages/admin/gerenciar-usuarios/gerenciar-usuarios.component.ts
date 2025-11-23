import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { FuncionarioService } from '../../../services/funcionario.service';
// IMPORTANTE: Importe o componente do Modal que criamos
import { UsuarioFormDialogComponent } from './usuario-form-dialog/usuario-form-dialog';

@Component({
  selector: 'app-gerenciar-usuarios',
  standalone: true,
  imports: [
    CommonModule, 
    MatCardModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatChipsModule,
    MatDialogModule,
    MatTooltipModule,
    MatSnackBarModule
  ],
  templateUrl: './gerenciar-usuarios.html',
  styleUrls: ['./gerenciar-usuarios.css']
})
export class GerenciarUsuariosComponent implements OnInit {
  usuarios: any[] = []; 
  displayedColumns: string[] = ['nome', 'cargo', 'perfil', 'status', 'acoes'];

  constructor(
    private funcionarioService: FuncionarioService, 
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() { 
    this.carregarUsuarios(); 
  }

  carregarUsuarios() {
    this.funcionarioService.listar().subscribe({
      next: (data) => this.usuarios = data,
      error: (err) => {
        console.error(err);
        this.snackBar.open('Erro ao carregar usuários.', 'Fechar');
      }
    });
  }

  abrirDialog(usuario?: any) {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '600px',
      maxWidth: '95vw',
      disableClose: true,
      data: { usuario: usuario } // Passa o usuário se for edição
    });

    dialogRef.afterClosed().subscribe(dadosFormulario => {
      // Se o usuário clicou em Salvar, 'dadosFormulario' terá o objeto JSON preenchido
      if (dadosFormulario) {
        
        if (usuario) {
          // === MODO EDIÇÃO (PUT) ===
          // Precisamos do ID do usuário original e os dados novos do formulário
          this.funcionarioService.atualizar(usuario.id, dadosFormulario).subscribe({
            next: () => {
              this.snackBar.open('Usuário atualizado com sucesso!', 'OK', { duration: 3000 });
              this.carregarUsuarios();
            },
            error: (err) => {
              console.error(err);
              this.snackBar.open('Erro ao atualizar usuário.', 'Fechar');
            }
          });

        } else {
          // === MODO CRIAÇÃO (POST) ===
          this.funcionarioService.criar(dadosFormulario).subscribe({
            next: () => {
              this.snackBar.open('Usuário criado com sucesso!', 'OK', { duration: 3000 });
              this.carregarUsuarios();
            },
            error: (err) => {
              console.error(err);
              this.snackBar.open('Erro ao criar usuário.', 'Fechar');
            }
          });
        }
      }
    });
  }

  deletar(id: number) {
    if(confirm('Tem certeza que deseja inativar este usuário?')) {
      this.funcionarioService.inativar(id).subscribe({
        next: () => {
          this.snackBar.open('Usuário inativado!', 'OK', { duration: 3000 });
          this.carregarUsuarios();
        },
        error: () => this.snackBar.open('Erro ao inativar.', 'Fechar')
      });
    }
  }

  getCorPerfil(perfil: string): string {
    if (perfil === 'ADMINISTRADOR') return 'warn';
    if (perfil === 'INSTRUTOR') return 'accent';
    return 'primary';
  }
}