import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
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

// ===============================
// INTERFACES
// ===============================

export interface OpcaoProcessada {
  chave: string;
  texto: string;
}

export interface QuestaoView {
  id: number;
  tipoQuestao: 'OBJETIVA' | 'DISSERTATIVA';
  enunciado: string;
  peso: number;
  ordem: number;

  opcoesProcessadas?: OpcaoProcessada[];
  respostaUsuario?: string;
}

export interface AvaliacaoDados {
  id: number;
  titulo: string;
  descricao: string;
  tempoLimiteMinutos: number;
  tentativasPermitidas: number;
  questoes: any[];
}

// ===============================
// COMPONENTE
// ===============================

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

  private http = inject(HttpClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  avaliacaoHeader: AvaliacaoDados | null = null;
  questoesView: QuestaoView[] = [];

  loading = true;
  enviando = false;

  tempoRestanteSegundos = 0;
  timerInterval: any;
  tempoFormatado = '--:--';
  timerCritico = false;

  // ===============================
  // CICLO DE VIDA
  // ===============================
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) this.carregarAvaliacao(Number(id));
  }

  ngOnDestroy(): void {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }

  // ===============================
  // CARREGAMENTO
  // ===============================
  carregarAvaliacao(id: number) {
    this.loading = true;

    this.http.get<AvaliacaoDados[]>(`http://localhost:8080/avaliacoes/${id}`)
      .subscribe({
        next: (arr) => {
          if (!arr || arr.length === 0) {
            this.mostrarErro('Avaliação não encontrada.');
            this.loading = false;
            return;
          }

          const dados = arr[0];
          this.avaliacaoHeader = dados;

          this.questoesView = this.processarQuestoesParaView(dados.questoes);
          this.iniciarTimer(dados.tempoLimiteMinutos * 60);

          this.loading = false;
        },
        error: () => {
          this.mostrarErro('Erro ao carregar a avaliação.');
          this.loading = false;
        }
      });
  }

  // ===============================
  // PROCESSAR QUESTÕES
  // ===============================
  private processarQuestoesParaView(questoesBrutas: any[]): QuestaoView[] {
    return questoesBrutas.map(q => {
      const view: QuestaoView = {
        id: q.id,
        tipoQuestao: q.tipoQuestao,
        enunciado: q.enunciado,
        peso: q.peso,
        ordem: q.ordem,
        respostaUsuario: ''
      };

      if (q.tipoQuestao === 'OBJETIVA' && q.opcoesResposta) {
        try {
          const obj = JSON.parse(q.opcoesResposta);
          view.opcoesProcessadas = Object.keys(obj).map(key => ({
            chave: key,
            texto: obj[key]
          }));
        } catch {
          view.opcoesProcessadas = [];
          console.error(`Erro ao parsear opções da questão ${q.id}`);
        }
      }

      return view;
    }).sort((a, b) => a.ordem - b.ordem);
  }

  // ===============================
  // TIMER
  // ===============================
  iniciarTimer(total: number) {
    this.tempoRestanteSegundos = total;
    this.atualizarDisplayTempo();

    this.timerInterval = setInterval(() => {
      this.tempoRestanteSegundos--;
      this.timerCritico = this.tempoRestanteSegundos < 300;

      if (this.tempoRestanteSegundos <= 0) {
        this.tempoRestanteSegundos = 0;
        clearInterval(this.timerInterval);
        this.finalizarAutomaticamente();
      }

      this.atualizarDisplayTempo();
    }, 1000);
  }

  atualizarDisplayTempo() {
    const m = Math.floor(this.tempoRestanteSegundos / 60);
    const s = this.tempoRestanteSegundos % 60;

    this.tempoFormatado =
      `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  }

  finalizarAutomaticamente() {
    this.snackBar.open('Tempo esgotado! Enviando suas respostas...', 'OK', { duration: 4000 });
    this.enviarAvaliacao();
  }

  // ===============================
  // ENVIO
  // ===============================
  confirmarEnvio() {
    const naoResp = this.questoesView.filter(q => !q.respostaUsuario).length;

    let msg = 'Deseja realmente finalizar a avaliação?';
    if (naoResp > 0)
      msg = `Atenção: você tem ${naoResp} questão(ões) sem resposta. Deseja enviar mesmo assim?`;

    if (confirm(msg)) this.enviarAvaliacao();
  }

  enviarAvaliacao() {
    if (this.timerInterval) clearInterval(this.timerInterval);

    this.enviando = true;
    this.loading = true;

    const payload = this.questoesView.map(q => ({
      questaoId: q.id,
      resposta: q.respostaUsuario
    }));

    const id = this.avaliacaoHeader?.id;

    this.http.post(`http://localhost:8080/avaliacoes/${id}/submeter`, payload)
      .subscribe({
        next: () => {
          this.snackBar.open('Avaliação enviada com sucesso!', 'OK', {
            duration: 3000,
            panelClass: ['snack-success']
          });
          this.router.navigate(['/minhas-avaliacoes']);
        },
        error: () => {
          this.mostrarErro('Erro ao enviar a avaliação.');
          this.enviando = false;
          this.loading = false;
        }
      });
  }

  // ===============================
  // SNACKBAR
  // ===============================
  mostrarErro(msg: string) {
    this.snackBar.open(msg, 'Fechar', {
      duration: 5000,
      panelClass: ['snack-error']
    });
  }
}
