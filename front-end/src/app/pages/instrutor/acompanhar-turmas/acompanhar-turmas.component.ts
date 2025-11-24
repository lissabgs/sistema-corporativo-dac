import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Necessário para o ngModel do filtro
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSelectModule } from '@angular/material/select'; // Importado para o filtro
import { MatFormFieldModule } from '@angular/material/form-field'; // Importado para o filtro

interface AlunoProgresso {
  id: number;
  nome: string;
  email: string;
  curso: string; // Adicionado: Matéria do aluno
  progresso: number;
  ultimoAcesso: Date;
  notaMedia: number;
  status: 'Ativo' | 'Inativo' | 'Concluido';
}

interface CorrecaoPendente {
  id: number;
  alunoNome: string;
  curso: string; // Adicionado
  atividadeTitulo: string;
  dataEnvio: Date;
  status: 'Pendente' | 'Em Correção';
}

@Component({
  selector: 'app-acompanhar-turmas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // Adicionado
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatTableModule,
    MatProgressBarModule,
    MatChipsModule,
    MatMenuModule,
    MatTooltipModule,
    MatBadgeModule,
    MatSelectModule, // Adicionado
    MatFormFieldModule // Adicionado
  ],
  templateUrl: './acompanhar-turmas.component.html',
  styleUrls: ['./acompanhar-turmas.component.css']
})
export class AcompanharTurmaComponent {

  // Filtro de Curso
  cursosDisponiveis = ['Todas as Matérias', 'Java Orientado a Objetos', 'Angular Material Essentials', 'Lógica de Programação'];
  cursoSelecionado = 'Todas as Matérias';

  // Dados Mockados - Agora com várias matérias misturadas
  todosAlunos: AlunoProgresso[] = [
    { id: 1, nome: 'Larisse Silva', email: 'larisse@empresa.com', curso: 'Java Orientado a Objetos', progresso: 85, ultimoAcesso: new Date(), notaMedia: 9.5, status: 'Ativo' },
    { id: 2, nome: 'João Pedro', email: 'joao@empresa.com', curso: 'Angular Material Essentials', progresso: 45, ultimoAcesso: new Date('2023-10-20'), notaMedia: 7.0, status: 'Ativo' },
    { id: 3, nome: 'Ana Costa', email: 'ana@empresa.com', curso: 'Java Orientado a Objetos', progresso: 100, ultimoAcesso: new Date('2023-10-15'), notaMedia: 10.0, status: 'Concluido' },
    { id: 4, nome: 'Marcos Souza', email: 'marcos@empresa.com', curso: 'Lógica de Programação', progresso: 10, ultimoAcesso: new Date('2023-09-10'), notaMedia: 0, status: 'Inativo' },
    { id: 5, nome: 'Pedro Henrique', email: 'pedro@empresa.com', curso: 'Angular Material Essentials', progresso: 60, ultimoAcesso: new Date(), notaMedia: 8.2, status: 'Ativo' },
  ];

  // Dados Mockados de Correções
  todasCorrecoes: CorrecaoPendente[] = [
    { id: 101, alunoNome: 'João Pedro', curso: 'Angular Material Essentials', atividadeTitulo: 'Projeto Final - Dashboard', dataEnvio: new Date(), status: 'Pendente' },
    { id: 102, alunoNome: 'Larisse Silva', curso: 'Java Orientado a Objetos', atividadeTitulo: 'Dissertativa - Ética', dataEnvio: new Date('2023-10-24'), status: 'Em Correção' },
    { id: 103, alunoNome: 'Fernanda Lima', curso: 'Lógica de Programação', atividadeTitulo: 'Exercício de Arrays', dataEnvio: new Date('2023-10-25'), status: 'Pendente' },
  ];

  // Colunas da Tabela (Adicionei 'curso')
  displayedColumnsAlunos: string[] = ['aluno', 'curso', 'progresso', 'nota', 'ultimoAcesso', 'status', 'acoes'];
  displayedColumnsCorrecoes: string[] = ['aluno', 'curso', 'atividade', 'data', 'status', 'acoes'];

  // Getters para filtrar automaticamente quando o select mudar
  get alunosFiltrados(): AlunoProgresso[] {
    if (this.cursoSelecionado === 'Todas as Matérias') {
      return this.todosAlunos;
    }
    return this.todosAlunos.filter(a => a.curso === this.cursoSelecionado);
  }

  get correcoesFiltradas(): CorrecaoPendente[] {
    if (this.cursoSelecionado === 'Todas as Matérias') {
      return this.todasCorrecoes;
    }
    return this.todasCorrecoes.filter(c => c.curso === this.cursoSelecionado);
  }

  enviarMensagem(aluno: AlunoProgresso) {
    alert(`Abrindo chat com ${aluno.nome}...`);
  }

  corrigirAtividade(correcao: CorrecaoPendente) {
    alert(`Iniciando correção de: ${correcao.atividadeTitulo}`);
  }

  getClassPorNota(nota: number): string {
    if (nota >= 9) return 'nota-alta';
    if (nota >= 7) return 'nota-media';
    return 'nota-baixa';
  }
}