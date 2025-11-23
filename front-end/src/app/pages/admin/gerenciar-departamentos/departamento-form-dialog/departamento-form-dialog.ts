import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { DepartamentoService } from '../../../../services/departamento.service';

@Component({
  selector: 'app-departamento-form-dialog',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './departamento-form-dialog.html',
  styleUrls: ['./departamento-form-dialog.css'] // Vamos criar esse CSS abaixo
})
export class DepartamentoFormDialogComponent {
  form: FormGroup;
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DepartamentoFormDialogComponent>,
    private service: DepartamentoService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.isEditMode = !!data?.departamento;
    const depto = data?.departamento;

    this.form = this.fb.group({
      id: [depto?.id || null],
      nome: [depto?.nome || '', Validators.required],
      codigo: [depto?.codigo || '', Validators.required],
      descricao: [depto?.descricao || '']
    });

    // REGRA DE NEGÓCIO: Na edição, o código não pode ser alterado
    if (this.isEditMode) {
      this.form.get('codigo')?.disable();
    }
  }

  onSubmit() {
    if (this.form.valid) {
      // getRawValue() pega até os campos desabilitados (importante para enviar o código mesmo que readonly)
      // Mas se o backend ignora o código na edição, tudo bem. 
      // Se for criação, pega tudo.
      const formValue = this.form.getRawValue();

      const request$ = this.isEditMode
        ? this.service.atualizar(formValue.id, formValue)
        : this.service.criar(formValue);

      request$.subscribe({
        next: () => {
          this.dialogRef.close(true); // Fecha retornando sucesso
        },
        error: (err) => console.error('Erro ao salvar departamento', err)
      });
    }
  }
}