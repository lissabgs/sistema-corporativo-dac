import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
// Imports para o Dialog
import { MatDialog, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FuncionarioService, Funcionario } from '../../../services/funcionario.service';

// --- Componente do Dialog de Confirmação (Modal) ---
@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>Confirmar Inativação</h2>
    <mat-dialog-content>
      <p>Tem certeza que deseja inativar o usuário <strong>{{ data.nome }}</strong>?</p>
      <p style="font-size: 12px; color: gray;">O usuário perderá o acesso ao sistema imediatamente.</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-raised-button color="warn" [mat-dialog-close]="true">Sim, Inativar</button>
    </mat-dialog-actions>
  `
})
export class ConfirmationDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { nome: string }) {}
}

// --- Componente Principal ---
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
    MatDialogModule // Importante adicionar aqui
  ],
  template: `
    <div class="page-container">
      <div class="header-actions">
        <h1>Gerenciar Usuários</h1>
        <button mat-raised-button color="primary" (click)="novoUsuario()">
          <mat-icon>person_add</mat-icon> Novo Usuário
        </button>
      </div>

      <mat-card class="table-card">
        <table mat-table [dataSource]="funcionarios">
          
          <ng-container matColumnDef="nome">
            <th mat-header-cell *matHeaderCellDef> Nome / Email </th>
            <td mat-cell *matCellDef="let f"> 
              <div class="font-bold">{{f.nome}}</div>
              <div class="text-muted">{{f.email}}</div>
            </td>
          </ng-container>

          <ng-container matColumnDef="cargo">
            <th mat-header-cell *matHeaderCellDef> Cargo / Depto </th>
            <td mat-cell *matCellDef="let f"> 
              {{f.cargo}} <br>
              <span class="badge-dept">{{f.departamentoNome}}</span>
            </td>
          </ng-container>

          <ng-container matColumnDef="perfil">
            <th mat-header-cell *matHeaderCellDef> Perfil </th>
            <td mat-cell *matCellDef="let f">
              <mat-chip [color]="getCorPerfil(f.perfil)" selected>{{ f.perfil }}</mat-chip>
            </td>
          </ng-container>

          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef> Status </th>
            <td mat-cell *matCellDef="let f">
              <span [style.color]="f.status ? 'green' : 'red'" style="font-weight: bold;">
                {{ f.status ? 'ATIVO' : 'INATIVO' }}
              </span>
            </td>
          </ng-container>

          <ng-container matColumnDef="acoes">
            <th mat-header-cell *matHeaderCellDef> Ações </th>
            <td mat-cell *matCellDef="let f">
              <button mat-icon-button color="primary" (click)="editar(f)" title="Editar">
                <mat-icon>edit</mat-icon>
              </button>
              <button mat-icon-button color="warn" *ngIf="f.status" (click)="inativar(f)" title="Inativar">
                <mat-icon>block</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="colunas"></tr>
          <tr mat-row *matRowDef="let row; columns: colunas;"></tr>
        </table>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container { padding: 24px; max-width: 1200px; margin: 0 auto; }
    .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
    .table-card { overflow: hidden; }
    table { width: 100%; }
    .font-bold { font-weight: 500; }
    .text-muted { font-size: 12px; color: #666; }
    .badge-dept { font-size: 11px; background: #f0f0f0; padding: 2px 6px; border-radius: 4px; }
  `]
})
export class GerenciarUsuariosComponent implements OnInit {
  funcionarios: Funcionario[] = [];
  colunas = ['nome', 'cargo', 'perfil', 'status', 'acoes'];

  constructor(
    private service: FuncionarioService, 
    private router: Router,
    private dialog: MatDialog // Injetando o MatDialog
  ) {}

  ngOnInit() { this.carregar(); }

  carregar() {
    this.service.listar().subscribe(data => this.funcionarios = data);
  }

  getCorPerfil(perfil: string): string {
    if (perfil === 'ADMINISTRADOR') return 'warn';
    if (perfil === 'INSTRUTOR') return 'accent';
    return 'primary';
  }

  novoUsuario() { this.router.navigate(['/form-usuario']); }

  editar(f: Funcionario) {
    this.router.navigate(['/form-usuario'], { state: { usuarioId: f.id } });
  }

  inativar(f: Funcionario) {
    // Abre o modal em vez do confirm nativo
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: { nome: f.nome } // Passa o nome para o modal
    });

    dialogRef.afterClosed().subscribe(result => {
      // Se result for true (clicou em Sim), executa a ação
      if (result === true) {
        this.service.inativar(f.id!).subscribe(() => this.carregar());
      }
    });
  }
}