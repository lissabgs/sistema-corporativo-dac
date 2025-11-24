import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';

// Módulos do Angular Material necessários para o design de dashboard
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTabsModule } from '@angular/material/tabs'; // NOVO: Para organização em abas
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// Interface para tipar os dados (Ajuda a evitar erros)
export interface Avaliacao {
  id: number;
  titulo: string;
  codigo?: string;
  status: 'PENDENTE' | 'AGUARDANDO_CORRECAO' | 'CONCLUIDA';
  dataEnvio?: string;     // Datas vêm como string ISO do backend
  dataConclusao?: string;
  nota?: number;
}

@Component({
  selector: 'app-minhas-avaliacoes',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    DatePipe // Importante para formatar datas no HTML
  ],
  templateUrl: './minhas-avaliacoes.component.html',
  styleUrls: ['./minhas-avaliacoes.component.css'],
})
export class MinhasAvaliacoesComponent implements OnInit {

  private http = inject(HttpClient);
  private router = inject(Router);

  // Usando a interface para tipagem segura
  pendentes: Avaliacao[] = [];
  aguardando: Avaliacao[] = [];
  concluidas: Avaliacao[] = [];

  loading = true;

  ngOnInit(): void {
    // OBS: Em um projeto real, mova esta chamada HTTP para um Service dedicado.
    this.http.get<Avaliacao[]>('http://localhost:8080/avaliacoes/funcionario')
      .subscribe({
        next: (lista) => {
          // Filtrando as listas para o dashboard
          this.pendentes = lista.filter(a => a.status === 'PENDENTE');
          this.aguardando = lista.filter(a => a.status === 'AGUARDANDO_CORRECAO');
          this.concluidas = lista.filter(a => a.status === 'CONCLUIDA');
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao buscar avaliações', err);
          // Aqui você poderia adicionar um Toast/SnackBar de erro
          this.loading = false;
        }
      });
  }

  iniciarAvaliacao(id: number) {
    this.router.navigate(['/avaliacao', id]);
  }
}