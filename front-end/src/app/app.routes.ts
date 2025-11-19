import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';
import { DashboardFuncionarioComponent } from './pages/funcionario/dashboard-funcionario/dashboard-funcionario.component';
import { CatalogoCursosComponent } from './pages/funcionario/catalogo-cursos/catalogo-cursos.component';
import { InscricaoCursoComponent } from './pages/funcionario/inscricao-curso/inscricao-curso.component';

// --- NOVOS COMPONENTES ---
import { DashboardInstrutorComponent } from './pages/instrutor/dashboard-instrutor/dashboard-instrutor.component';
import { GerenciarCursosComponent } from './pages/instrutor/gerenciar-cursos/gerenciar-cursos.component';
import { CadastrarCursoComponent } from './pages/instrutor/cadastrar-curso/cadastrar-curso.component';
import { AcessoNegadoComponent } from './pages/acesso-negado/acesso-negado.component'; // <-- NOVO

// --- NOVO GUARD ---
import { roleGuard } from './guards/auth.guard'; // <-- NOVO

// Perfis disponíveis no sistema: 'FUNCIONARIO', 'INSTRUTOR', 'ADMINISTRADOR'

// Todas as rotas agora usam o roleGuard para garantir que o usuário está logado e tem o perfil necessário.
const ALL_ROLES = ['FUNCIONARIO', 'INSTRUTOR', 'ADMINISTRADOR'];
const INSTRUCTOR_OR_ADMIN_ROLES = ['INSTRUTOR', 'ADMINISTRADOR'];


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'autocadastro', component: AutocadastroComponent },
  { path: 'acesso-negado', component: AcessoNegadoComponent }, // Rota de Acesso Negado

  // ROTAS FUNCIONÁRIO (Acessível por todos os perfis logados)
  { 
    path: 'dashboard-funcionario', 
    component: DashboardFuncionarioComponent,
    canActivate: [roleGuard(ALL_ROLES)]
  },
  { 
    path: 'catalogo-cursos', 
    component: CatalogoCursosComponent,
    canActivate: [roleGuard(ALL_ROLES)]
  },
  { 
    path: 'inscricao-curso', 
    component: InscricaoCursoComponent,
    canActivate: [roleGuard(ALL_ROLES)]
  },

  // ROTAS INSTRUTOR/ADMIN (Acessível apenas por INSTRUTOR e ADMINISTRADOR)
  { 
    path: 'dashboard-instrutor', 
    component: DashboardInstrutorComponent,
    canActivate: [roleGuard(INSTRUCTOR_OR_ADMIN_ROLES)] 
  },
  { 
    path: 'gerenciar-cursos', 
    component: GerenciarCursosComponent,
    canActivate: [roleGuard(INSTRUCTOR_OR_ADMIN_ROLES)]
  },
  { 
    path: 'cadastrar-curso', 
    component: CadastrarCursoComponent,
    canActivate: [roleGuard(INSTRUCTOR_OR_ADMIN_ROLES)]
  },
  
  // Rota curinga para redirecionar usuários logados para o dashboard apropriado
  // Isso requer lógica no AppComponent ou um canActivate, mas por agora, apenas um redirecionamento simples
  // Ou mantenha as rotas bem definidas acima.
];