import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // redireciona raiz -> /login
  { path: 'login', component: LoginComponent },
  { path: 'autocadastro', component: AutocadastroComponent } // nova rota
];
