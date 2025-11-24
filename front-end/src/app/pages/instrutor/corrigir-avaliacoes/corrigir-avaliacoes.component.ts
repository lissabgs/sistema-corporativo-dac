import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardInstrutorService } from '../../../services/dashboard-instrutor.service';
import { CorrecaoService } from '../../../services/correcao.service';
import { TentativaPendente, Correcao } from '../../../models/correcao.model';

@Component({
  selector: 'app-corrigir-avaliacoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './corrigir-avaliacoes.component.html',
  styleUrls: ['./corrigir-avaliacoes.component.css']
})
export class CorrigirAvaliacoesComponent implements OnInit {
  pendentes: TentativaPendente[] = [];
  historico: Correcao[] = [];
  abaAtiva: string = 'pendentes';
  
  instrutorId: number = 0; // Removido o fixo

  tentativaParaCorrigir: TentativaPendente | null = null;
  notaInput: number | null = null;
  feedbackInput: string = '';

  constructor(
    private dashboardService: DashboardInstrutorService,
    private correcaoService: CorrecaoService
  ) {}

  ngOnInit(): void {
    // CORREÇÃO: Busca ID real
    const idSalvo = localStorage.getItem('usuarioId');
    if (idSalvo) {
      this.instrutorId = Number(idSalvo);
      this.carregarDados();
    }
  }

  carregarDados() {
    this.carregarPendencias();
    this.carregarHistorico();
  }

  carregarPendencias() {
    this.dashboardService.getDashboard(this.instrutorId).subscribe(dash => {
      this.pendentes = dash.tentativasPendentes || [];
    });
  }

  carregarHistorico() {
    this.correcaoService.listarMinhasCorrecoes(this.instrutorId).subscribe(data => {
      this.historico = data;
    });
  }

  abrirCorrecao(item: TentativaPendente) {
    this.tentativaParaCorrigir = item;
    this.notaInput = null;
    this.feedbackInput = '';
  }

  enviarCorrecao() {
    if (!this.tentativaParaCorrigir || this.notaInput === null) {
      alert('Preencha a nota e o feedback.');
      return;
    }
    this.correcaoService.corrigirQuestao(
      this.tentativaParaCorrigir.id,
      this.instrutorId,
      this.feedbackInput,
      this.notaInput
    ).subscribe({
      next: () => {
        alert('Correção enviada!');
        this.tentativaParaCorrigir = null;
        this.carregarDados();
      },
      error: () => alert('Erro ao enviar.')
    });
  }

  cancelar() {
    this.tentativaParaCorrigir = null;
  }
}