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
import { HttpClient } from '@angular/common/http'; // 1. Importar HttpClient

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

  // URL do Gateway
  private apiUrl = 'http://localhost:8080/api/funcionarios/autocadastro';

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private departamentoService: DepartamentoService,
    private http: HttpClient // 2. Injetar HttpClient
  ) {
    this.formCadastro = this.fb.group({
      cpf: ['', Validators.required],
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      departamento_id: ['', Validators.required],
      cargo: ['', Validators.required],
      // Removi campos que não são inputs do usuário (xp, nivel, senha, status)
    });
  }

  ngOnInit(): void {
    this.carregarDepartamentos();
  }

  carregarDepartamentos() {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (dados) => {
        this.departamentos = dados;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar departamentos.', 'Fechar', {
          duration: 3000
          // panelClass: ['snackbar-error'] // opcional se tiver CSS
        });
      }
    });
  }

  // 3. Lógica de Formatação do CPF (Mascara)
  formatarCPF(event: any) {
    let valor = event.target.value;

    // Remove tudo que não é dígito (bloqueia letras)
    valor = valor.replace(/\D/g, '');

    // Limita a 11 dígitos
    if (valor.length > 11) {
      valor = valor.substring(0, 11);
    }

    // Aplica a máscara 000.000.000-00
    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

    // Atualiza o valor no formulário
    this.formCadastro.get('cpf')?.setValue(valor, { emitEvent: false });
  }

  onSubmit() {
    if (this.formCadastro.valid) {
      const rawValues = this.formCadastro.value;

      // 4. Montar o Payload CORRETO para o Java
      const payload = {
        cpf: rawValues.cpf, // Envia formatado (o Java limpa) ou limpe aqui se preferir
        nome: rawValues.nome,
        email: rawValues.email,
        cargo: rawValues.cargo,
        departamentoId: rawValues.departamento_id, // Renomeando _id -> Id
        perfil: 'FUNCIONARIO' // Adicionando perfil fixo
        // Senha foi removida
      };

      console.log('Enviando JSON:', payload);

      // 5. Enviar requisição POST
      this.http.post(this.apiUrl, payload).subscribe({
        next: (res) => {
          console.log('Sucesso:', res);
          this.snackBar.open('Cadastro realizado! Verifique seu e-mail (logs) para a senha.', 'Fechar', {
            duration: 5000,
            panelClass: ['snackbar-success']
          });
          // Redireciona após 2s
          setTimeout(() => this.voltarLogin(), 2000);
        },
        error: (err) => {
          console.error('Erro ao cadastrar:', err);
          this.snackBar.open('Erro ao realizar cadastro. Tente novamente.', 'Fechar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }
      });

    } else {
      this.snackBar.open('Preencha todos os campos obrigatórios.', 'Fechar', {
        duration: 3000
      });
    }
  }

  voltarLogin() {
    this.router.navigate(['/login']);
  }
}