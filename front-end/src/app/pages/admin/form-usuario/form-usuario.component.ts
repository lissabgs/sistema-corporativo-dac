import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FuncionarioService } from '../../../services/funcionario.service';
import { DepartamentoService, Departamento } from '../../../services/departamento.service';

@Component({
  selector: 'app-form-usuario',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatCardModule, 
    MatFormFieldModule, MatInputModule, MatSelectModule, 
    MatButtonModule, MatSnackBarModule
  ],
  template: `
    <div class="container-form">
      <mat-card class="card-form">
        <h2>{{ isEdit ? 'Editar Usuário' : 'Cadastrar Novo Usuário' }}</h2>
        
        <form [formGroup]="form" (ngSubmit)="salvar()">
          
          <div class="grid-2">
            <mat-form-field appearance="outline">
              <mat-label>Nome Completo</mat-label>
              <input matInput formControlName="nome">
              <mat-error>Obrigatório</mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>CPF</mat-label>
              <input matInput 
                     formControlName="cpf" 
                     placeholder="000.000.000-00"
                     maxlength="14"
                     (input)="aoDigitarCPF($event)">
              <mat-error>Obrigatório</mat-error>
            </mat-form-field>
          </div>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>E-mail</mat-label>
            <input matInput formControlName="email" type="email">
            <mat-error>E-mail inválido</mat-error>
          </mat-form-field>

          <div class="grid-2">
            <mat-form-field appearance="outline">
              <mat-label>Departamento</mat-label>
              <mat-select formControlName="departamentoId">
                <mat-option *ngFor="let d of departamentos" [value]="d.id">{{ d.nome }}</mat-option>
              </mat-select>
              <mat-error>Obrigatório</mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Cargo</mat-label>
              <input matInput formControlName="cargo">
              <mat-error>Obrigatório</mat-error>
            </mat-form-field>
          </div>

          <div class="grid-2">
            <mat-form-field appearance="outline">
              <mat-label>Perfil</mat-label>
              <mat-select formControlName="perfil">
                <mat-option value="FUNCIONARIO">Funcionário</mat-option>
                <mat-option value="INSTRUTOR">Instrutor</mat-option>
                <mat-option value="ADMINISTRADOR">Administrador</mat-option>
              </mat-select>
              <mat-error>Obrigatório</mat-error>
            </mat-form-field>
            
          </div>

          <div class="actions">
            <button mat-button type="button" (click)="cancelar()">Cancelar</button>
            <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
              {{ isEdit ? 'Salvar Alterações' : 'Criar Usuário' }}
            </button>
          </div>

        </form>
      </mat-card>
    </div>
  `,
  styles: [`
    .container-form { display: flex; justify-content: center; padding: 40px; background: #f5f5f5; min-height: 90vh; }
    .card-form { width: 100%; max-width: 800px; padding: 30px; border-radius: 12px; }
    h2 { margin-bottom: 25px; color: #333; font-weight: 500; }
    .grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .full-width { width: 100%; }
    .actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
  `]
})
export class FormUsuarioComponent implements OnInit {
  form: FormGroup;
  isEdit = false;
  usuarioId: number | null = null;
  departamentos: Departamento[] = [];

  constructor(
    private fb: FormBuilder,
    private funcService: FuncionarioService,
    private deptService: DepartamentoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      nome: ['', Validators.required],
      cpf: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      departamentoId: ['', Validators.required],
      cargo: ['', Validators.required],
      perfil: ['FUNCIONARIO', Validators.required]
    });
  }

  ngOnInit() {
    this.deptService.listarDepartamentos().subscribe(d => this.departamentos = d);

    const state = history.state;
    if (state && state.usuarioId) {
      this.isEdit = true;
      this.usuarioId = state.usuarioId;
      this.form.get('cpf')?.disable(); // CPF não se edita
      
      this.funcService.obterPorId(this.usuarioId!).subscribe(dados => {
        // --- CORREÇÃO AQUI ---
        // Se o CPF vier do banco, aplicamos a máscara antes de jogar no form
        if (dados.cpf) {
          dados.cpf = this.aplicarMascara(dados.cpf);
        }
        this.form.patchValue(dados);
      });
    }
  }

  // 1. Método auxiliar para formatar CPF vindo do banco (string completa)
  private aplicarMascara(cpf: string): string {
    // Remove tudo que não é número
    const valor = cpf.replace(/\D/g, '');
    // Aplica a máscara 000.000.000-00
    return valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  // 2. Método para formatar enquanto o usuário digita (Input Event)
  aoDigitarCPF(event: any) {
    let valor = event.target.value;
    valor = valor.replace(/\D/g, '');
    
    if (valor.length > 11) {
      valor = valor.substring(0, 11);
    }
    
    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    
    this.form.get('cpf')?.setValue(valor, { emitEvent: false });
  }

  salvar() {
    if (this.form.invalid) return;
    
    const dados = this.form.getRawValue(); 

    // Limpa a máscara antes de enviar para o backend
    if (dados.cpf) {
        dados.cpf = dados.cpf.replace(/\D/g, '');
    }

    if (this.isEdit && this.usuarioId) {
      this.funcService.atualizar(this.usuarioId, dados).subscribe({
        next: () => this.sucesso('Usuário atualizado!'),
        error: (err) => this.erro('Erro ao atualizar', err)
      });
    } else {
      this.funcService.criar(dados).subscribe({
        next: () => this.sucesso('Usuário criado! Senha enviada por e-mail.'),
        error: (err) => this.erro('Erro ao criar', err)
      });
    }
  }

  sucesso(msg: string) {
    this.snackBar.open(msg, 'OK', { duration: 4000 });
    this.router.navigate(['/gerenciar-usuarios']);
  }

  erro(msg: string, err: any) {
    console.error(err);
    const msgErro = err.error?.erro || 'Falha técnica no servidor.';
    this.snackBar.open(`${msg}: ${msgErro}`, 'Fechar', { duration: 5000 });
  }

  cancelar() { this.router.navigate(['/gerenciar-usuarios']); }
}