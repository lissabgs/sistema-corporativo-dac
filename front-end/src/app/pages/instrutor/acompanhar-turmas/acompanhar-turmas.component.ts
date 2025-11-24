import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CursoService } from '../../../services/curso.service';
import { AvaliacaoService } from '../../../services/avaliacao.service';
import { TentativaService } from '../../../services/tentativa.service';
import { Curso } from '../../../models/curso.model';
import { Avaliacao } from '../../../models/avaliacao.model';
import { Tentativa } from '../../../models/tentativa.model';

interface ProgressoTurma {
  funcionarioNome: string;
  cursoTitulo: string;
  progresso: number;
  status: 'Concluído' | 'Em Andamento';
  modulosConcluidos?: number;
  totalModulos?: number;
  ultimaAtividade?: string;
}

@Component({
  selector: 'app-acompanhar-turmas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './acompanhar-turmas.component.html',
  styleUrls: ['./acompanhar-turmas.component.css']
})
export class AcompanharTurmasComponent implements OnInit {
  progressoTurmas: ProgressoTurma[] = [];
  turmaSelecionada: ProgressoTurma | null = null;

  cursos: Curso[] = [];
  avaliacoes: Avaliacao[] = [];
  tentativas: Tentativa[] = [];
  cursoSelecionado: Curso | null = null;
  avaliacaoSelecionada: Avaliacao | null = null;
  instrutorId: number = 0;

  constructor(
    private cursoService: CursoService,
    private avaliacaoService: AvaliacaoService,
    private tentativaService: TentativaService
  ) {}

  ngOnInit(): void {
    const idSalvo = localStorage.getItem('usuarioId');
    if (idSalvo) {
      this.instrutorId = Number(idSalvo);
      this.carregarCursos();
      this.carregarDadosTabela();
    } else {
      console.warn('Nenhum usuário logado. Usando dados de teste.');
      this.carregarDadosTabela();
    }
  }

  carregarCursos() {
    this.cursoService.listarPorInstrutor(this.instrutorId).subscribe({
      next: (data) => this.cursos = data,
      error: (err) => console.error('Erro ao buscar cursos:', err)
    });
  }

  carregarDadosTabela(): void {
    this.progressoTurmas = [
      { funcionarioNome: 'Ana Silva', cursoTitulo: 'Java Spring Boot', progresso: 100, status: 'Concluído', modulosConcluidos: 10, totalModulos: 10, ultimaAtividade: '23/11/2025 - Certificado Emitido' },
      { funcionarioNome: 'Carlos Oliveira', cursoTitulo: 'Angular Avançado', progresso: 45, status: 'Em Andamento', modulosConcluidos: 4, totalModulos: 9, ultimaAtividade: '20/11/2025 - Aula: Rotas e Guardas' },
      { funcionarioNome: 'Mariana Santos', cursoTitulo: 'Microserviços', progresso: 70, status: 'Em Andamento', modulosConcluidos: 7, totalModulos: 10, ultimaAtividade: '24/11/2025 - Aula: Docker Compose' },
      { funcionarioNome: 'Roberto Costa', cursoTitulo: 'Docker & Kubernetes', progresso: 10, status: 'Em Andamento', modulosConcluidos: 1, totalModulos: 12, ultimaAtividade: '15/11/2025 - Aula: Instalação' }
    ];
  }

  verDetalhes(item: ProgressoTurma): void {
    this.turmaSelecionada = item;
  }

  fecharModal(): void {
    this.turmaSelecionada = null;
  }

  // Métodos herdados (caso precise no futuro)
  selecionarCurso(curso: Curso) {
    this.cursoSelecionado = curso;
    this.avaliacaoSelecionada = null;
    this.tentativas = [];
    this.avaliacaoService.listar().subscribe(data => {
      this.avaliacoes = data.filter(av => av.cursoId === curso.id);
    });
  }

  exportarRelatorio() {
    // Lógica de exportação futura
  }
}