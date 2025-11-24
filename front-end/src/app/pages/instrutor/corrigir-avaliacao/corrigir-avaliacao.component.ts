import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// Importando o serviço que criamos acima
import { CorrecaoService } from '../../../services/correcao.service';

@Component({
  selector: 'app-corrigir-avaliacao',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    RouterModule,
    MatCardModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './corrigir-avaliacao.component.html',
  styleUrls: ['./corrigir-avaliacao.component.css']
})
export class CorrigirAvaliacaoComponent implements OnInit {
  tentativa: any = null;
  tentativaId!: number;
  loading = true;
  
  // Array que armazenará as notas e comentários de cada questão
  correcoes: any[] = []; 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private correcaoService: CorrecaoService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    // 1. Pega o ID da URL
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.tentativaId = +idParam;
      this.carregarTentativa();
    } else {
      this.voltar();
    }
  }

  carregarTentativa() {
    this.loading = true;
    this.correcaoService.getTentativaParaCorrecao(this.tentativaId).subscribe({
      next: (data) => {
        this.tentativa = data;
        
        // Inicializa o array de correções com base nas respostas que vieram do backend
        this.correcoes = this.tentativa.respostas.map((resposta: any) => ({
          respostaId: resposta.id,
          questaoId: resposta.questao.id,
          // Se já tiver nota (correção prévia), usa ela, senão 0
          notaAtribuida: resposta.notaObtida || 0,
          comentarios: resposta.feedback || ''
        }));
        
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar tentativa', err);
        this.snackBar.open('Erro ao carregar a prova do aluno.', 'Fechar', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  calcularNotaTotal(): number {
    if (!this.correcoes) return 0;
    return this.correcoes.reduce((acc, curr) => acc + (curr.notaAtribuida || 0), 0);
  }

  salvarCorrecao() {
    // Validação simples: Nota não pode ser maior que o máximo da questão
    for (let i = 0; i < this.correcoes.length; i++) {
      const correcao = this.correcoes[i];
      const max = this.tentativa.respostas[i].questao.notaMaxima;
      if (correcao.notaAtribuida > max) {
        this.snackBar.open(`A nota da questão ${i + 1} não pode ser maior que ${max}.`, 'OK');
        return;
      }
    }

    const payload = {
      tentativaId: this.tentativaId,
      itensCorrecao: this.correcoes
    };

    this.loading = true;
    this.correcaoService.enviarCorrecao(payload).subscribe({
      next: () => {
        this.snackBar.open('Correção salva com sucesso!', 'Fechar', { duration: 3000 });
        // Redireciona de volta para a lista de avaliações do curso
        // Como não temos o ID do curso fácil aqui, voltamos para o gerenciamento geral ou tentamos pegar do histórico
        // Uma boa prática seria ter o ID do curso na resposta da tentativa. Vamos supor que temos:
        if (this.tentativa && this.tentativa.cursoId) {
            this.router.navigate(['/instrutor/curso', this.tentativa.cursoId, 'avaliacoes']);
        } else {
            this.router.navigate(['/instrutor/gerenciar-cursos']);
        }
      },
      error: (err) => {
        this.snackBar.open('Erro ao salvar correção.', 'Fechar', { duration: 3000 });
        console.error(err);
        this.loading = false;
      }
    });
  }

  voltar() {
    // Tenta voltar para a página anterior no histórico do navegador ou vai para home
    if (window.history.length > 1) {
      window.history.back();
    } else {
      this.router.navigate(['/instrutor/gerenciar-cursos']);
    }
  }
}