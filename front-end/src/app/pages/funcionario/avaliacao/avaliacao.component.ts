import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

interface OpcaoProcessada {
  chave: string;
  texto: string;
}

interface QuestaoView {
  id: number;
  tipoQuestao: 'OBJETIVA' | 'DISSERTATIVA';
  enunciado: string;
  peso: number;
  ordem: number;

  opcoesProcessadas?: OpcaoProcessada[];
  respostaUsuario?: string;
}

interface AvaliacaoDados {
  id: number;
  titulo: string;
  descricao: string;
  tempoLimiteMinutos: number;
  tentativasPermitidas: number;
  questoes: QuestaoView[];
}

@Component({
  selector: 'app-realizar-avaliacao',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,

    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatRadioModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './avaliacao.component.html',
  styleUrls: ['./avaliacao.component.css']
})
export class RealizarAvaliacaoComponent implements OnInit, OnDestroy {

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);

  avaliacaoHeader: AvaliacaoDados | null = null;
  questoesView: QuestaoView[] = [];

  loading = true;
  enviando = false;

  tempoRestanteSegundos = 0;
  timerInterval: any = null;
  tempoFormatado = '--:--';
  timerCritico = false;

  // ===============================
  // INICIALIZAÇÃO
  // ===============================
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.carregarMockAvaliacao(id);
  }

  ngOnDestroy(): void {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }

  // ===============================
  // MOCK (SEM BACKEND)
  // ===============================
  carregarMockAvaliacao(id: number) {
    this.loading = true;

    this.avaliacaoHeader = {
      id,
      titulo: "Avaliação de Java Básico (DEMO)",
      descricao: "Esta prova é carregada localmente para testes do front-end.",
      tempoLimiteMinutos: 10,
      tentativasPermitidas: 1,
      questoes: [
        {
          id: 1,
          tipoQuestao: 'OBJETIVA',
          enunciado: "O que é uma classe em Java?",
          peso: 1,
          ordem: 1,
          opcoesProcessadas: [
            { chave: 'A', texto: "Um arquivo CSS" },
            { chave: 'B', texto: "Um molde para criar objetos" },
            { chave: 'C', texto: "Um banco de dados" },
            { chave: 'D', texto: "Uma função recursiva" }
          ]
        },
        {
          id: 2,
          tipoQuestao: 'OBJETIVA',
          enunciado: "Qual palavra-chave instancia um objeto em Java?",
          peso: 1,
          ordem: 2,
          opcoesProcessadas: [
            { chave: 'A', texto: "make" },
            { chave: 'B', texto: "object" },
            { chave: 'C', texto: "new" },
            { chave: 'D', texto: "instance" }
          ]
        },
        {
          id: 3,
          tipoQuestao: 'DISSERTATIVA',
          enunciado: "Explique o conceito de encapsulamento.",
          peso: 2,
          ordem: 3
        }
      ]
    };

    this.questoesView = [...this.avaliacaoHeader.questoes];

    // Inicia o timer
    this.iniciarTimer(this.avaliacaoHeader.tempoLimiteMinutos * 60);

    this.loading = false;
  }

  // ===============================
  // TIMER
  // ===============================
  iniciarTimer(segundos: number) {
    this.tempoRestanteSegundos = segundos;
    this.atualizarTempo();

    this.timerInterval = setInterval(() => {
      if (this.tempoRestanteSegundos <= 0) {
        clearInterval(this.timerInterval);
        this.enviarAutomaticamente();
        return;
      }

      this.tempoRestanteSegundos--;
      this.timerCritico = this.tempoRestanteSegundos < 300;
      this.atualizarTempo();
    }, 1000);
  }

  atualizarTempo() {
    const m = Math.floor(this.tempoRestanteSegundos / 60);
    const s = this.tempoRestanteSegundos % 60;

    this.tempoFormatado =
      `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  }

  enviarAutomaticamente() {
    this.snackBar.open("Tempo esgotado! Enviando respostas...", "OK", {
      duration: 3000
    });
    this.finalizar();
  }

  // ===============================
  // FINALIZAÇÃO (SIMULADA)
  // ===============================
  confirmarEnvio() {
    const faltando = this.questoesView.filter(q => !q.respostaUsuario).length;

    let msg = "Deseja finalizar a avaliação?";
    if (faltando > 0)
      msg = `Você deixou ${faltando} questão(ões) em branco. Enviar mesmo assim?`;

    if (confirm(msg)) this.finalizar();
  }

  finalizar() {
    if (this.timerInterval) clearInterval(this.timerInterval);

    this.enviando = true;

    setTimeout(() => {
      this.snackBar.open("Avaliação enviada com sucesso! (modo DEMO)", "OK", {
        duration: 3000
      });

      this.router.navigate(['/minhas-avaliacoes']);
    }, 1500);
  }
}
