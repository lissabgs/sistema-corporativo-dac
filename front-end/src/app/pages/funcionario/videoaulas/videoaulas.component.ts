import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

// Material Imports
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';

// IMPORTANDO SEU MODEL OFICIAL
import { Curso, Aula } from '../../../models/curso.model'; 

@Component({
  selector: 'app-videoaulas',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatExpansionModule,
    MatButtonModule
  ],
  templateUrl: './videoaulas.component.html',
  styleUrls: ['./videoaulas.component.css']
})
export class VideoaulasComponent implements OnInit {
  private sanitizer = inject(DomSanitizer);

  // Usando a tipagem do seu model
  curso: Curso | null = null;
  aulaAtual: Aula | null = null;
  urlVideoSegura: SafeResourceUrl | null = null;

  ngOnInit() {
    this.carregarDadosMock();
  }

  selecionarAula(aula: Aula) {
    this.aulaAtual = aula;
    this.urlVideoSegura = this.gerarUrlEmbedSegura(aula.urlConteudo);
    
    // Scroll suave para o topo
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  private gerarUrlEmbedSegura(url: string): SafeResourceUrl {
    let videoId = '';
    
    if (!url) return '';

    if (url.includes('v=')) {
      videoId = url.split('v=')[1];
      const ampersandPosition = videoId.indexOf('&');
      if (ampersandPosition !== -1) {
        videoId = videoId.substring(0, ampersandPosition);
      }
    } else if (url.includes('youtu.be/')) {
       videoId = url.split('youtu.be/')[1];
    }

    // Se não achou ID, retorna vazio ou url original (tratamento de erro simples)
    if (!videoId) return this.sanitizer.bypassSecurityTrustResourceUrl(url);

    const embedUrl = `https://www.youtube.com/embed/${videoId}?rel=0&autoplay=1`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }

  // --- MOCK ADAPTADO AO SEU MODEL ---
  private carregarDadosMock() {
    // Simulando a resposta do backend exatamente como seu JSON
    const responseBackend: Curso = {
      id: 2,
      codigo: "TES-2345",
      titulo: "Curso de Teste Financeiro", // Ajustei o titulo para ficar bonito
      descricao: "Aprenda os conceitos fundamentais...",
      categoriaId: "Financeiro",
      duracaoEstimada: "1h",
      instrutorId: 2,
      xpOferecido: 343,
      nivelDificuldade: "Iniciante", // Ajuste conforme seu Enum ou string
      status: "ATIVO",
      preRequisitos: [],
      modulos: [
        {
          id: 11,
          titulo: "Introdução ao Sistema",
          ordem: 1,
          aulas: [
            { 
              id: 13, 
              titulo: "Aula 1: Boas vindas", 
              descricao: "", 
              urlConteudo: "https://www.youtube.com/watch?v=sDEE_NGBe4s", 
              ordem: 1, 
              obrigatorio: true, 
              xpModulo: 10 
            },
            { 
              id: 14, 
              titulo: "Aula 2: Configuração Inicial", 
              descricao: "", 
              urlConteudo: "https://www.youtube.com/watch?v=sDEE_NGBe4s", 
              ordem: 2, 
              obrigatorio: true, 
              xpModulo: 10 
            }
          ]
        },
        {
          id: 12,
          titulo: "Módulo Avançado",
          ordem: 2,
          aulas: [
            { 
              id: 15, 
              titulo: "Aula 3: Análise de Dados", 
              descricao: "", 
              urlConteudo: "https://www.youtube.com/watch?v=sDEE_NGBe4s", 
              ordem: 1, 
              obrigatorio: true, 
              xpModulo: 20 
            }
          ]
        }
      ]
    };

    this.curso = responseBackend;

    // Inicializa a primeira aula
    if (this.curso.modulos?.length > 0 && this.curso.modulos[0].aulas?.length > 0) {
      this.selecionarAula(this.curso.modulos[0].aulas[0]);
    }
  }
}