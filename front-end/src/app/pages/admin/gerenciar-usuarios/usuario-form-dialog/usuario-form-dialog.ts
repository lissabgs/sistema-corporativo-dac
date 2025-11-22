import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { DepartamentoService } from '../../../../services/departamento.service';

@Component({
  selector: 'app-usuario-form-dialog',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule,
    MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule
  ],
  template: `
    <h2 mat-dialog-title>{{ data ? 'Editar' : 'Novo' }} Usuário</h2>
    <mat-dialog-content>
      <form [formGroup]="form" style="display: flex; flex-direction: column; gap: 10px; min-width: 300px;">
        
        <mat-form-field appearance="outline">
          <mat-label>Nome</mat-label>
          <input matInput formControlName="nome">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>E-mail</mat-label>
          <input matInput formControlName="email">
        </mat-form-field>

        <div style="display: flex; gap: 10px;">
          <mat-form-field appearance="outline" style="flex: 1;">
            <mat-label>CPF</mat-label>
            <input matInput formControlName="cpf">
          </mat-form-field>

          <mat-form-field appearance="outline" style="flex: 1;">
            <mat-label>Cargo</mat-label>
            <input matInput formControlName="cargo">
          </mat-form-field>
        </div>

        <div style="display: flex; gap: 10px;">
          <mat-form-field appearance="outline" style="flex: 1;">
            <mat-label>Departamento</mat-label>
            <mat-select formControlName="departamentoId">
              <mat-option *ngFor="let d of departamentos" [value]="d.id">{{ d.nome }}</mat-option>
            </mat-select>
          </mat-form-field>

          <mat-form-field appearance="outline" style="flex: 1;">
            <mat-label>Perfil</mat-label>
            <mat-select formControlName="perfil">
              <mat-option value="FUNCIONARIO">Funcionário</mat-option>
              <mat-option value="INSTRUTOR">Instrutor</mat-option>
              <mat-option value="ADMINISTRADOR">Administrador</mat-option>
            </mat-select>
          </mat-form-field>
        </div>

        <mat-form-field appearance="outline" *ngIf="!data">
            <mat-label>Senha Inicial</mat-label>
            <input matInput formControlName="senha">
        </mat-form-field>

      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button mat-raised-button color="primary" (click)="salvar()" [disabled]="form.invalid">Salvar</button>
    </mat-dialog-actions>
  `
})
export class UsuarioFormDialogComponent implements OnInit {
  form: FormGroup;
  departamentos: any[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UsuarioFormDialogComponent>,
    private departamentoService: DepartamentoService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.form = this.fb.group({
      nome: [data?.nome || '', Validators.required],
      email: [data?.email || '', [Validators.required, Validators.email]],
      cpf: [data?.cpf || '', Validators.required],
      cargo: [data?.cargo || '', Validators.required],
      departamentoId: [data?.departamentoId || '', Validators.required],
      perfil: [data?.perfil || 'FUNCIONARIO', Validators.required],
      senha: ['1234'] 
    });

    if(data) {
        this.form.get('cpf')?.disable();
        this.form.removeControl('senha'); 
    }
  }

  ngOnInit() {
    this.departamentoService.listarDepartamentos().subscribe(res => this.departamentos = res);
  }

  salvar() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.getRawValue());
    }
  }
}