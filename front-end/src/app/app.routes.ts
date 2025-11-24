import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';
import { DashboardFuncionarioComponent } from './pages/funcionario/dashboard-funcionario/dashboard-funcionario.component';
import { CatalogoCursosComponent } from './pages/funcionario/catalogo-cursos/catalogo-cursos.component';
import { InscricaoCursoComponent } from './pages/funcionario/inscricao-curso/inscricao-curso.component';
import { DashboardInstrutorComponent } from './pages/instrutor/dashboard-instrutor/dashboard-instrutor.component';
import { GerenciarCursosComponent } from './pages/instrutor/gerenciar-cursos/gerenciar-cursos.component';
import { CadastrarCursoComponent } from './pages/instrutor/cadastrar-curso/cadastrar-curso.component';
import { GerenciarAvaliacoesComponent } from './pages/instrutor/gerenciar-avaliacoes/gerenciar-avaliacoes.component';
import { AcessoNegadoComponent } from './pages/acesso-negado/acesso-negado.component';
import { DashboardAdminComponent } from './pages/admin/dashboard-admin/dashboard-admin.component';
import { GerenciarUsuariosComponent } from './pages/admin/gerenciar-usuarios/gerenciar-usuarios.component'; 
import { FormUsuarioComponent } from './pages/admin/form-usuario/form-usuario.component'; 
import { GerenciarDepartamentosComponent } from './pages/admin/gerenciar-departamentos/gerenciar-departamentos.component'; 

// --- NOVOS COMPONENTES (Certifique-se que foram criados) ---
import { ListarAvaliacoesCursoComponent } from './pages/instrutor/listar-avaliacoes-curso/listar-avaliacoes-curso.component';
import { CorrigirAvaliacaoComponent } from './pages/instrutor/corrigir-avaliacao/corrigir-avaliacao.component';
import { AcompanharTurmasComponent } from './pages/instrutor/acompanhar-turmas/acompanhar-turmas.component';

import { roleGuard } from './guards/auth.guard';

const APENAS_FUNCIONARIO = ['FUNCIONARIO'];
const APENAS_INSTRUTOR = ['INSTRUTOR']; 
const APENAS_ADMIN = ['ADMINISTRADOR']; 

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
    path: 'catalogo-cursos',
    component: CatalogoCursosComponent,
    canActivate: [roleGuard(APENAS_FUNCIONARIO)]
  },
  { 
    path: 'inscricao-curso', 
    component: InscricaoCursoComponent,
    canActivate: [roleGuard(APENAS_FUNCIONARIO)]
  },

  // --- ROTAS DO INSTRUTOR ---
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
  // Rota duplicada para compatibilidade (com e sem prefixo)
  { 
    path: 'cadastrar-curso', 
    component: CadastrarCursoComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  { 
    path: 'instrutor/cadastrar-curso', 
    component: CadastrarCursoComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  {
    path: 'gerenciar-avaliacoes', 
    component: GerenciarAvaliacoesComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  
  // === ROTAS NOVAS (CORREÇÃO DE PROVAS) ===
  
  // 1. Rota do Menu "Acompanhar Turmas" (Resolve o problema de Logout)
  {
    path: 'acompanhar-turmas', 
    component: AcompanharTurmasComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },

  // 2. Rota da Prancheta (Lista de alunos que fizeram a prova)
  {
    path: 'instrutor/curso/:id/avaliacoes', 
    component: ListarAvaliacoesCursoComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },
  
  // 3. Rota da Tela de Correção (Prova individual)
  {
    path: 'instrutor/corrigir-tentativa/:id', 
    component: CorrigirAvaliacaoComponent,
    canActivate: [roleGuard(APENAS_INSTRUTOR)]
  },

  // --- ROTAS DE ADMIN ---
  {
    path: 'dashboard-admin',
    component: DashboardAdminComponent,
    canActivate: [roleGuard(APENAS_ADMIN)]
  },
  {
    path: 'gerenciar-usuarios',
    component: GerenciarUsuariosComponent,
    canActivate: [roleGuard(APENAS_ADMIN)]
  },
  {
    path: 'form-usuario',
    component: FormUsuarioComponent,
    canActivate: [roleGuard(APENAS_ADMIN)]
  },
  {
    path: 'gerenciar-departamentos',
    component: GerenciarDepartamentosComponent,
    canActivate: [roleGuard(APENAS_ADMIN)] 
  },

  // Wildcard: Redireciona qualquer rota desconhecida para login
  { path: '**', redirectTo: 'login' }
];