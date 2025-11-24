import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http'; // Mantido caso queira descomentar depois
import { Router, RouterModule } from '@angular/router';

// Módulos do Angular Material necessários para o design de dashboard
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// Interface para tipar os dados
export interface Avaliacao {
  id: number;
  titulo: string;
  codigo?: string;
  status: 'PENDENTE' | 'AGUARDANDO_CORRECAO' | 'CONCLUIDA';
  dataEnvio?: string;     // Datas vêm como string ISO
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

  pendentes: Avaliacao[] = [];
  aguardando: Avaliacao[] = [];
  concluidas: Avaliacao[] = [];

  loading = true;

  ngOnInit(): void {
    // Simula um delay de rede para mostrar o loading (opcional, mas fica bonito)
    setTimeout(() => {
      this.carregarDadosMockados();
      this.loading = false;
    }, 800);
  }

  carregarDadosMockados() {
    // === DADOS MOCKADOS (VISUAL) ===
    
    // 1. Avaliações Pendentes (Incluindo a de Java que você pediu)
    this.pendentes = [
      {
        id: 101,
        titulo: 'Java Completo: POO e Spring Boot',
        codigo: 'JAVA-2024',
        status: 'PENDENTE'
      },
      {
        id: 102,
        titulo: 'Segurança da Informação Básica',
        codigo: 'SEC-001',
        status: 'PENDENTE'
      }
    ];

    // 2. Aguardando Correção
    this.aguardando = [
      {
        id: 201,
        titulo: 'Arquitetura de Microservices',
        status: 'AGUARDANDO_CORRECAO',
        dataEnvio: new Date(new Date().setDate(new Date().getDate() - 2)).toISOString() // Enviado há 2 dias
      }
    ];

    // 3. Concluídas (Com nota)
    this.concluidas = [
      {
        id: 301,
        titulo: 'Lógica de Programação',
        status: 'CONCLUIDA',
        dataConclusao: '2024-10-15T14:30:00',
        nota: 9.5
      },
      {
        id: 302,
        titulo: 'Git e GitHub para Iniciantes',
        status: 'CONCLUIDA',
        dataConclusao: '2024-09-20T10:00:00',
        nota: 10.0
      },
      {
        id: 303,
        titulo: 'Scrum e Metodologias Ágeis',
        status: 'CONCLUIDA',
        dataConclusao: '2024-08-05T16:45:00',
        nota: 8.0
      }
    ];
  }

  iniciarAvaliacao(id: number) {
    // Redireciona para o componente de realizar avaliação
    // Passando o ID da avaliação (ex: 101 para Java)
    this.router.navigate(['/avaliacao', id]);
  }
}