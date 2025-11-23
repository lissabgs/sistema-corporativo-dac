import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DepartamentoService, Departamento } from '../../../services/departamento.service';

// --- Dialog para Criar/Editar ---
@Component({
  selector: 'dialog-departamento',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatDialogModule
  ],
  template: `
    <h2 mat-dialog-title>{{ isEdit ? 'Editar Departamento' : 'Novo Departamento' }}</h2>
    
    <mat-dialog-content>
      <form [formGroup]="form" class="flex-col">
        
        <mat-form-field appearance="outline" class="w-full">
          <mat-label>Código (Sigla)</mat-label>
          <input matInput formControlName="codigo" placeholder="EX: TI">
          <mat-error>Obrigatório</mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-full">
          <mat-label>Nome</mat-label>
          <input matInput formControlName="nome" placeholder="Tecnologia da Informação">
          <mat-error>Obrigatório</mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="w-full">
          <mat-label>Descrição</mat-label>
          <textarea matInput formControlName="descricao" rows="3"></textarea>
        </mat-form-field>

      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-raised-button color="primary" 
              [disabled]="form.invalid" 
              [mat-dialog-close]="form.value">
        Salvar
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .w-full { width: 100%; } 
    .flex-col { display: flex; flex-direction: column; gap: 16px; min-width: 350px; padding-top: 10px; }
  `]
})
export class DialogDepartamentoComponent {
  form: FormGroup;
  isEdit: boolean;

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: Departamento | null
  ) {
    this.isEdit = !!data; // Se data existe, é edição

    this.form = this.fb.group({
      codigo: [data?.codigo || '', Validators.required],
      nome: [data?.nome || '', Validators.required],
      descricao: [data?.descricao || ''],
      // Mantemos o gestorId oculto ou nulo se não for editar agora
      gestorId: [data?.gestorId || null]
    });
  }
}

// --- Componente Principal ---
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
    MatSnackBarModule
  ],
  template: `
    <div class="page-container">
      <div class="header-actions">
        <h1>Departamentos</h1>
        <button mat-raised-button color="primary" (click)="abrirDialog()">
          <mat-icon>add</mat-icon> Novo Departamento
        </button>
      </div>

      <mat-card class="table-card">
        <table mat-table [dataSource]="departamentos">
          
          <ng-container matColumnDef="codigo">
            <th mat-header-cell *matHeaderCellDef> Código </th>
            <td mat-cell *matCellDef="let element"> <strong>{{element.codigo}}</strong> </td>
          </ng-container>

          <ng-container matColumnDef="nome">
            <th mat-header-cell *matHeaderCellDef> Nome </th>
            <td mat-cell *matCellDef="let element"> {{element.nome}} </td>
          </ng-container>

          <ng-container matColumnDef="descricao">
            <th mat-header-cell *matHeaderCellDef> Descrição </th>
            <td mat-cell *matCellDef="let element"> {{element.descricao}} </td>
          </ng-container>

          <ng-container matColumnDef="acoes">
            <th mat-header-cell *matHeaderCellDef class="text-right"> Ações </th>
            <td mat-cell *matCellDef="let element" class="text-right">
              <button mat-stroked-button color="primary" (click)="abrirDialog(element)">
                <mat-icon>edit</mat-icon> Editar
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
        
        <div *ngIf="departamentos.length === 0" class="empty-state">
          Nenhum departamento cadastrado.
        </div>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container { padding: 24px; max-width: 1000px; margin: 0 auto; }
    .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
    .table-card { overflow: hidden; }
    table { width: 100%; }
    .empty-state { padding: 40px; text-align: center; color: #777; font-style: italic; }
    .text-right { text-align: right; }
    /* Ajuste para a coluna de ações não ficar colada */
    td.mat-cell:last-of-type { padding-right: 24px; }
  `]
})
export class GerenciarDepartamentosComponent implements OnInit {
  departamentos: Departamento[] = [];
  displayedColumns: string[] = ['codigo', 'nome', 'descricao', 'acoes'];

  constructor(
    private deptService: DepartamentoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.carregarDados();
  }

  carregarDados() {
    this.deptService.listarDepartamentos().subscribe({
      next: (dados) => this.departamentos = dados,
      error: (err) => console.error('Erro ao carregar departamentos', err)
    });
  }

  // Abre o modal para criar (sem param) ou editar (com param)
  abrirDialog(dept?: Departamento) {
    const dialogRef = this.dialog.open(DialogDepartamentoComponent, {
      width: '400px',
      data: dept || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Se o departamento original tinha ID, é uma edição
        if (dept && dept.id) {
          this.atualizarDepartamento(dept.id, result);
        } else {
          this.criarDepartamento(result);
        }
      }
    });
  }

  criarDepartamento(dados: Departamento) {
    this.deptService.criar(dados).subscribe({
      next: () => {
        this.snackBar.open('Departamento criado com sucesso!', 'OK', { duration: 3000 });
        this.carregarDados();
      },
      error: () => this.snackBar.open('Erro ao criar departamento.', 'Fechar')
    });
  }

  atualizarDepartamento(id: number, dados: Departamento) {
    this.deptService.atualizar(id, dados).subscribe({
      next: () => {
        this.snackBar.open('Departamento atualizado com sucesso!', 'OK', { duration: 3000 });
        this.carregarDados();
      },
      error: () => this.snackBar.open('Erro ao atualizar departamento.', 'Fechar')
    });
  }
}