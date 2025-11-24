import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CursoService } from '../../../services/curso.service';
import { AvaliacaoService } from '../../../services/avaliacao.service';
import { TentativaService } from '../../../services/tentativa.service';
import { Curso } from '../../../models/curso.model';
import { Avaliacao } from '../../../models/avaliacao.model';
import { Tentativa } from '../../../models/tentativa.model';

@Component({
  selector: 'app-acompanhar-turmas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './acompanhar-turmas.component.html',
  styleUrls: ['./acompanhar-turmas.component.css']
})
export class AcompanharTurmasComponent implements OnInit {
  cursos: Curso[] = [];
  avaliacoes: Avaliacao[] = [];
  tentativas: Tentativa[] = [];
  
  cursoSelecionado: Curso | null = null;
  avaliacaoSelecionada: Avaliacao | null = null;
  
  instrutorId: number = 0; // Removido o fixo = 1

  constructor(
    private cursoService: CursoService,
    private avaliacaoService: AvaliacaoService,
    private tentativaService: TentativaService
  ) {}

  ngOnInit(): void {
    // CORREÇÃO: Pega o ID salvo no navegador durante o login
    const idSalvo = localStorage.getItem('usuarioId');
    
    if (idSalvo) {
      this.instrutorId = Number(idSalvo);
      this.carregarCursos();
    } else {
      console.warn('Nenhum usuário logado encontrado.');
    }
  }

  carregarCursos() {
    this.cursoService.listarPorInstrutor(this.instrutorId).subscribe({
      next: (data) => {
        this.cursos = data;
        // console.log('Cursos carregados:', data); // Descomente para debug se precisar
      },
      error: (err) => console.error('Erro ao buscar cursos:', err)
    });
  }

  selecionarCurso(curso: Curso) {
    this.cursoSelecionado = curso;
    this.avaliacaoSelecionada = null;
    this.tentativas = [];
    
    this.avaliacaoService.listar().subscribe(data => {
      // --- CORREÇÃO AQUI ---
      // Filtra o array: só mantém as avaliações onde 'cursoId' é igual ao 'id' do curso clicado
      this.avaliacoes = data.filter(av => av.cursoId === curso.id);
      
      // (Opcional) Debug para ver se funcionou
      console.log('Avaliações filtradas:', this.avaliacoes);
    });
  }

  selecionarAvaliacao(avaliacao: Avaliacao) {
    this.avaliacaoSelecionada = avaliacao;
    if(avaliacao.id) {
        this.tentativaService.listarPorAvaliacao(avaliacao.id).subscribe(data => {
          this.tentativas = data;
        });
    }
  }

  voltar() {
    this.cursoSelecionado = null;
    this.avaliacaoSelecionada = null;
  }

  exportarRelatorio() {
    const csvContent = "data:text/csv;charset=utf-8," 
      + "AlunoID,Nota,Status\n"
      + this.tentativas.map(t => `${t.funcionarioId},${t.nota},${t.status}`).join("\n");
    const encodedUri = encodeURI(csvContent);
    window.open(encodedUri);
  }
}