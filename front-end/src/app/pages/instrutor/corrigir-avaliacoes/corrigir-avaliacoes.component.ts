import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardInstrutorService } from '../../../services/dashboard-instrutor.service';
import { CorrecaoService } from '../../../services/correcao.service';
import { Correcao } from '../../../models/correcao.model'; 

@Component({
  selector: 'app-corrigir-avaliacoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './corrigir-avaliacoes.component.html',
  styleUrls: ['./corrigir-avaliacoes.component.css']
})
export class CorrigirAvaliacoesComponent implements OnInit {
  tentativasPendentes: any[] = []; 
  historico: Correcao[] = [];
  instrutorId: number = 0;

  tentativaParaCorrigir: any | null = null;
  notaInput: number | null = null;
  feedbackInput: string = '';

  constructor(
    private dashboardService: DashboardInstrutorService,
    private correcaoService: CorrecaoService
  ) {}

  ngOnInit(): void {
    const idSalvo = localStorage.getItem('usuarioId');
    if (idSalvo) {
      this.instrutorId = Number(idSalvo);
      this.carregarDados();
    } else {
      console.warn('Usuário não logado. Usando ID 1 para teste.');
      this.instrutorId = 1; 
      this.carregarDados();
    }
  }

  carregarDados() {
    this.carregarPendencias();
    this.carregarHistorico();
  }

  carregarPendencias() {
    this.dashboardService.getDashboard(this.instrutorId).subscribe({
      next: (dash) => {
        this.tentativasPendentes = dash.tentativasPendentes || [];
      },
      error: (err) => {
        console.error('Erro ao carregar dashboard', err);
        this.tentativasPendentes = [
          { id: 1, funcionarioNome: 'João Teste (Mock)', avaliacaoTitulo: 'Java Básico - Final', dataFim: new Date(), funcionarioId: 99 }
        ];
      }
    });
  }

  carregarHistorico() {
    this.correcaoService.listarMinhasCorrecoes(this.instrutorId).subscribe({
      next: (data) => this.historico = data,
      error: (err) => console.error('Erro ao carregar histórico', err)
    });
  }

  iniciarCorrecao(item: any): void {
    this.tentativaParaCorrigir = item;
    this.notaInput = null;
    this.feedbackInput = '';
  }

  enviarCorrecao() {
    if (!this.tentativaParaCorrigir || this.notaInput === null) {
      alert('Por favor, preencha a nota e o feedback.');
      return;
    }

    this.correcaoService.corrigirQuestao(
      this.tentativaParaCorrigir.id,
      this.instrutorId,
      this.feedbackInput,
      this.notaInput
    ).subscribe({
      next: () => {
        alert('Correção enviada com sucesso!');
        this.tentativaParaCorrigir = null;
        this.carregarDados();
      },
      error: (err) => {
        console.error(err);
        alert('Erro ao enviar correção. Tente novamente.');
      }
    });
  }

  cancelar() {
    this.tentativaParaCorrigir = null;
  }
}