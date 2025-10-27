import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DepartamentoService } from '../../../services/departamento.service';

@Component({
  selector: 'app-autocadastro',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatSelectModule
  ],
  templateUrl: './autocadastro.component.html',
  styleUrls: ['./autocadastro.component.css']
})
export class AutocadastroComponent implements OnInit {
  formCadastro: FormGroup;
  departamentos: any[] = [];
  dominioEmpresa = 'empresa.com.br';
  emailInvalido = false;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private departamentoService: DepartamentoService
  ) {
    this.formCadastro = this.fb.group({
      cpf: ['', Validators.required],
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      departamento_id: ['', Validators.required],
      cargo: ['', Validators.required],
      xp: [0],
      nivel: ['Iniciante'],
      status: ['ATIVO'],
      senha: ['']
    });
  }

  ngOnInit(): void {
    this.carregarDepartamentos();
  }

 //puxar os departamentos do backend
  
  /*carregarDepartamentos() {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (dados) => {
        this.departamentos = dados;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar departamentos.', 'Fechar', {
          duration: 3000
        });
      }
    });
  }
  */

  // enquanto o backend não está pronto
  carregarDepartamentos() {
    this.departamentos = [
      { id: 1, nome: 'Financeiro' },
      { id: 2, nome: 'Recursos Humanos' },
      { id: 3, nome: 'Tecnologia da Informação' },
      { id: 4, nome: 'Comercial' }
    ];
  }

  onSubmit() {
    if (this.formCadastro.valid) {
      console.log('Dados do formulário:', this.formCadastro.value);

      this.snackBar.open('Cadastro realizado com sucesso!', 'Fechar', {
        duration: 3000,
        panelClass: ['snackbar-success']
      });

      this.formCadastro.reset({
        xp: 0,
        nivel: 'Iniciante',
        status: 'ATIVO'
      });
    } else {
      this.snackBar.open('Preencha todos os campos obrigatórios.', 'Fechar', {
        duration: 3000,
        panelClass: ['snackbar-error']
      });
    }
  }

  voltarLogin() {
    this.router.navigate(['/login']);
  }
}
