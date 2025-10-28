import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';
import { DashboardFuncionarioComponent } from './pages/funcionario/dashboard-funcionario/dashboard-funcionario.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'autocadastro', component: AutocadastroComponent },
  { path: 'dashboard-funcionario', component: DashboardFuncionarioComponent }
];
