import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';

type Curso = {
  id: number;
  titulo: string;
  categoria: string;
  progresso?: number; // 0-100 (somente em andamento)
};

type Rank = { nome: string; cargo: string; xp: number; };

type Atividade = {
  titulo: string;
  data: Date | string;
  icone: string;
  xpDelta: number;
};

@Component({
  selector: 'app-dashboard-funcionario',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatCardModule,
    MatListModule,
    MatDividerModule,
    MatProgressBarModule,
    MatChipsModule
  ],
  templateUrl: './dashboard-funcionario.component.html',
  styleUrls: ['./dashboard-funcionario.component.css']
})
export class DashboardFuncionarioComponent {
  // KPI / Gamificação
  xpAtual = 1320;
  nivel = 5;
  xpParaProximoNivel = 1600; // limite do nível atual
  get progressoNivel() {
    const pct = (this.xpAtual / this.xpParaProximoNivel) * 100;
    return Math.max(0, Math.min(100, pct));
  }

  proximoBadge = { nome: 'Aprendiz Avançado', requisitoXp: 1600 };

  // Cursos
  cursosEmAndamento: Curso[] = [
    { id: 1, titulo: 'Angular Material Essentials', categoria: 'Frontend', progresso: 40 },
    { id: 2, titulo: 'RxJS Prático', categoria: 'Frontend', progresso: 65 }
  ];

  cursosConcluidos: Curso[] = [
    { id: 3, titulo: 'Git & GitHub Workflow', categoria: 'Dev Tools' },
    { id: 4, titulo: 'Fundamentos de TypeScript', categoria: 'Linguagens' }
  ];

  cursosDisponiveis: Curso[] = [
    { id: 5, titulo: 'Clean Architecture', categoria: 'Arquitetura' },
    { id: 6, titulo: 'Testes com Jasmine/Karma', categoria: 'Qualidade' }
  ];

  // Ranking
  ranking: Rank[] = [
    { nome: 'Você', cargo: 'Dev Frontend', xp: 1320 },
    { nome: 'Ana', cargo: 'Dev Pleno', xp: 1750 },
    { nome: 'Carlos', cargo: 'QA', xp: 920 },
    { nome: 'Marina', cargo: 'PO', xp: 2110 }
  ].sort((a, b) => b.xp - a.xp);

  // Timeline
  atividades: Atividade[] = [
    { titulo: 'Concluiu módulo: Form Controls', data: new Date(), icone: 'check_circle', xpDelta: 80 },
    { titulo: 'Aula assistida: Observables', data: new Date(Date.now() - 1000 * 60 * 60 * 6), icone: 'play_arrow', xpDelta: 30 },
    { titulo: 'Recebeu badge: Produtividade', data: new Date(Date.now() - 1000 * 60 * 60 * 24), icone: 'emoji_events', xpDelta: 120 },
    { titulo: 'Tentativa de quiz', data: new Date(Date.now() - 1000 * 60 * 60 * 30), icone: 'quiz', xpDelta: 0 }
  ];

  // Ações do menu
  iniciarCurso(c?: Curso) { console.log('Iniciar curso', c ?? '(selecionar)'); }
  continuarCurso() { console.log('Continuar curso em andamento'); }
  verCertificados() { console.log('Abrir certificados'); }
  abrirLojaBadges() { console.log('Abrir loja de badges'); }

  baixarCertificado(curso: Curso) {
    console.log('Baixar certificado de', curso.titulo);
  }
}
