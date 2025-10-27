import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-login',
  standalone: true, // 👈 obrigatório para standalone
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  formLogin: FormGroup;
  hide = true;

  constructor(private fb: FormBuilder) {
    this.formLogin = this.fb.group({
      usuario: ['', Validators.required],
      senha: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.formLogin.valid) {
      const { usuario, senha } = this.formLogin.value;
      console.log('Usuário:', usuario);
      console.log('Senha:', senha);
      // aqui você implementa a lógica de login real
    } else {
      this.formLogin.markAllAsTouched();
    }
  }
}
