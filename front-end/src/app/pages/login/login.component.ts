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
    private snackBar: MatSnackBar     
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
        console.log('Login bem-sucedido', response);
        
        // --- LÓGICA DE REDIRECIONAMENTO BASEADA NO PERFIL ---
        const perfil = response.perfil;

        if (perfil === 'INSTRUTOR' || perfil === 'ADMINISTRADOR') {
          this.router.navigate(['/dashboard-instrutor']);
        } else {
          this.router.navigate(['/dashboard-funcionario']);
        }
      },
      error: (err) => {
        console.error('Erro no login', err);
        this.snackBar.open('E-mail ou senha inválidos.', 'Fechar', {
          duration: 3000,
          panelClass: ['snackbar-error'] 
        });
      }
    });
  }
}