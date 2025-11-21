import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';
import { DashboardFuncionarioComponent } from './pages/funcionario/dashboard-funcionario/dashboard-funcionario.component';
import { CatalogoCursosComponent } from './pages/funcionario/catalogo-cursos/catalogo-cursos.component';
import { InscricaoCursoComponent } from './pages/funcionario/inscricao-curso/inscricao-curso.component';
import { DashboardInstrutorComponent } from './pages/instrutor/dashboard-instrutor/dashboard-instrutor.component';
import { GerenciarCursosComponent } from './pages/instrutor/gerenciar-cursos/gerenciar-cursos.component';
import { CadastrarCursoComponent } from './pages/instrutor/cadastrar-curso/cadastrar-curso.component';
import { AcessoNegadoComponent } from './pages/acesso-negado/acesso-negado.component';
import { DashboardAdminComponent } from './pages/admin/dashboard-admin/dashboard-admin.component';
import { roleGuard } from './guards/auth.guard';

const APENAS_FUNCIONARIO = ['FUNCIONARIO'];
const APENAS_INSTRUTOR = ['INSTRUTOR']; 
const APENAS_ADMIN = ['ADMINISTRADOR']; 
const GESTORES = ['INSTRUTOR', 'ADMINISTRADOR'];

export const routes: Routes = [
  // Rotas Públicas
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'autocadastro', component: AutocadastroComponent },
  { path: 'acesso-negado', component: AcessoNegadoComponent },

  // --- ROTAS EXCLUSIVAS DE FUNCIONÁRIO ---
  { 
    path: 'dashboard-funcionario', 
    component: DashboardFuncionarioComponent,
    canActivate: [roleGuard(APENAS_FUNCIONARIO)] 
  },
  { 
    path: 'catalogo-cursos', // <--- Agora restrita apenas para FUNCIONARIO
    component: CatalogoCursosComponent,
    canActivate: [roleGuard(APENAS_FUNCIONARIO)]
  },
  { 
    path: 'inscricao-curso', 
    component: InscricaoCursoComponent,
    canActivate: [roleGuard(APENAS_FUNCIONARIO)]
  },

  // --- ROTAS DO INSTRUTOR / ADMIN ---
  { 
    path: 'dashboard-instrutor', 
    component: DashboardInstrutorComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)] 
  },
  { 
    path: 'gerenciar-cursos', 
    component: GerenciarCursosComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  { 
    path: 'cadastrar-curso', 
    component: CadastrarCursoComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  {
    path: 'dashboard-admin',
    component: DashboardAdminComponent,
    canActivate: [roleGuard(APENAS_ADMIN)]
  },
];