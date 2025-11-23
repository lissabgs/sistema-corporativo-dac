import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon'; // <--- Adicionado para os ícones
import { DepartamentoService } from '../../../../services/departamento.service';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask'; // Opcional: Se usar máscara de CPF

@Component({
  selector: 'app-usuario-form-dialog',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatSelectModule,
    MatIconModule, // <--- Importante
    NgxMaskDirective // Se estiver usando a máscara no HTML
  ],
  providers: [provideNgxMask()],
  // Aponta para os arquivos externos para pegar o estilo correto
  templateUrl: './usuario-form-dialog.html',
  styleUrls: ['./usuario-form-dialog.css']
})
export class UsuarioFormDialogComponent implements OnInit {
  form: FormGroup;
  departamentos: any[] = [];
  isEditMode: boolean = false; // Variável para controlar título do HTML externo

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UsuarioFormDialogComponent>,
    private departamentoService: DepartamentoService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    // Verifica se é edição baseado nos dados recebidos
    this.isEditMode = !!data?.usuario;
    const usuario = data?.usuario;

    this.form = this.fb.group({
      nome: [usuario?.nome || '', Validators.required],
      email: [usuario?.email || '', [Validators.required, Validators.email]],
      cpf: [usuario?.cpf || '', Validators.required],
      cargo: [usuario?.cargo || '', Validators.required],
      departamentoId: [usuario?.departamentoId || '', Validators.required],
      perfil: [usuario?.perfil || 'FUNCIONARIO', Validators.required],
      // Senha não é obrigatória na edição
      senha: [''] 
    });

    if(this.isEditMode) {
        this.form.get('cpf')?.disable();
        // Remove validação de senha se for edição (geralmente senha não se edita aqui)
        this.form.get('senha')?.clearValidators();
        this.form.get('senha')?.updateValueAndValidity();
    }
  }

  ngOnInit() {
    this.departamentoService.listarDepartamentos().subscribe(res => this.departamentos = res);
  }

  onSubmit() {
    if (this.form.valid) {
      // getRawValue inclui campos desabilitados (como CPF)
      this.dialogRef.close(this.form.getRawValue());
    }
  }
}