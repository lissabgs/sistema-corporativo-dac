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
// Importação correta baseada no seu arquivo: usuario-form-dialog.ts
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
  // CORREÇÃO: Removido .component dos nomes dos arquivos
  templateUrl: './gerenciar-usuarios.html',
  styleUrls: ['./gerenciar-usuarios.css']
})
export class GerenciarUsuariosComponent implements OnInit {
  // Use 'any' ou o seu DTO 'UsuarioResponseDTO'
  usuarios: any[] = []; 
  
  // Colunas que correspondem ao HTML novo
  displayedColumns: string[] = ['nome', 'cargo', 'perfil', 'status', 'acoes'];

  constructor(
    private funcionarioService: FuncionarioService, 
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() { 
    this.carregarUsuarios(); 
  }

  // O método agora se chama carregarUsuarios corretamente
  carregarUsuarios() {
    this.funcionarioService.listar().subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (err) => {
        console.error('Erro ao listar usuários', err);
        this.snackBar.open('Erro ao carregar usuários.', 'Fechar');
      }
    });
  }

  // Método para abrir o Modal (Criação ou Edição)
  abrirDialog(usuario?: any) {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '600px',      // Largura fixa para ficar bonito
      maxWidth: '95vw',    // Responsividade
      disableClose: true,  // Obriga a usar os botões
      data: { usuario: usuario } // Passa os dados se for edição
    });

    dialogRef.afterClosed().subscribe(result => {
      // Se o modal retornar true (salvou com sucesso), recarrega a lista
      if (result) {
        this.carregarUsuarios();
      }
    });
  }

  // Método de Inativar/Excluir
  deletar(id: number) {
    // Pode substituir por um modal de confirmação mais bonito depois se quiser
    if(confirm('Tem certeza que deseja inativar este usuário? Ele perderá o acesso.')) {
      this.funcionarioService.inativar(id).subscribe({
        next: () => {
          this.snackBar.open('Usuário inativado com sucesso!', 'OK', { duration: 3000 });
          this.carregarUsuarios();
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao inativar usuário.', 'Fechar');
        }
      });
    }
  }

  // Auxiliar para cores dos chips
  getCorPerfil(perfil: string): string {
    if (perfil === 'ADMINISTRADOR') return 'warn'; // Vermelho
    if (perfil === 'INSTRUTOR') return 'accent';   // Rosa/Amarelo (depende do tema)
    return 'primary'; // Azul
  }
}