import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http'; // Importar HTTP

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    RouterModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  formLogin: FormGroup;
  hide = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar,
    private http: HttpClient // Injeção para buscar dados do usuário
  ) {
    this.formLogin = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.formLogin.invalid) {
      this.formLogin.markAllAsTouched();
      return;
    }

    this.authService.login(this.formLogin.value).subscribe({
      next: (response) => {
        const id = response.usuarioId;
        const perfil = response.perfil;

        // --- NOVO: Busca dados detalhados (Nome/Cargo) antes de redirecionar ---
        this.http.get<any>(`http://localhost:8080/api/funcionarios/${id}`, {
          headers: { 'Authorization': `Bearer ${response.token}` }
        }).subscribe({
          next: (usuario) => {
            // Salva Nome e Cargo no Storage
            localStorage.setItem('usuarioNome', usuario.nome);
            localStorage.setItem('usuarioCargo', usuario.cargo);

            this.redirecionar(perfil);
          },
          error: () => {
            // Se falhar em pegar detalhes, redireciona mesmo assim com dados genéricos
            localStorage.setItem('usuarioNome', 'Usuário');
            localStorage.setItem('usuarioCargo', 'Colaborador');
            this.redirecionar(perfil);
          }
        });
      },
      error: (err) => {
        this.snackBar.open('E-mail ou senha inválidos.', 'Fechar', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
      }
    });
  }

  redirecionar(perfil: string) {
    if (perfil === 'ADMINISTRADOR') {
      this.router.navigate(['/dashboard-admin']);
    } else if (perfil === 'INSTRUTOR') {
      this.router.navigate(['/dashboard-instrutor']);
    } else {
      this.router.navigate(['/dashboard-funcionario']);
    }
  }
}