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

  // --- AQUI ESTÁ A MÁGICA DO MODAL ---
  abrirDialog(usuario?: any) {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '700px',      // Largura controlada para ficar com a proporção correta
      maxWidth: '95vw',    // Responsividade para celular
      disableClose: true,  // Obriga clicar em cancelar/salvar
      autoFocus: false,    // Evita focar no primeiro input automaticamente (opcional)
      data: { usuario: usuario } // Passa os dados (se for edição) ou null (se for novo)
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarUsuarios(); // Recarrega a lista se salvou
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