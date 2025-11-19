import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AutocadastroComponent } from './pages/funcionario/autocadastro/autocadastro.component';
import { DashboardFuncionarioComponent } from './pages/funcionario/dashboard-funcionario/dashboard-funcionario.component';
import { CatalogoCursosComponent } from './pages/funcionario/catalogo-cursos/catalogo-cursos.component';
import { InscricaoCursoComponent } from './pages/funcionario/inscricao-curso/inscricao-curso.component';

// --- NOVOS COMPONENTES DO INSTRUTOR ---
import { DashboardInstrutorComponent } from './pages/instrutor/dashboard-instrutor/dashboard-instrutor.component';
import { GerenciarCursosComponent } from './pages/instrutor/gerenciar-cursos/gerenciar-cursos.component';
import { CadastrarCursoComponent } from './pages/instrutor/cadastrar-curso/cadastrar-curso.component';


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'autocadastro', component: AutocadastroComponent },
  { path: 'dashboard-funcionario', component: DashboardFuncionarioComponent },
  { path: 'catalogo-cursos', component: CatalogoCursosComponent },
  { path: 'inscricao-curso', component: InscricaoCursoComponent },

  // ROTAS INSTRUTOR
  { path: 'dashboard-instrutor', component: DashboardInstrutorComponent },
  { path: 'gerenciar-cursos', component: GerenciarCursosComponent },
  { path: 'cadastrar-curso', component: CadastrarCursoComponent }
];