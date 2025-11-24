import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

// --- CORREÇÃO AQUI: Importar MatCardModule ---
import { MatCardModule } from '@angular/material/card';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { CursoService } from '../../../services/curso.service';
import { ProgressoService } from '../../../services/progresso.service';
import { Curso, Aula } from '../../../models/curso.model';

@Component({
  selector: 'app-videoaulas',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule, // <--- ADICIONAR AQUI TAMBÉM NA LISTA DE IMPORTS
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatExpansionModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './videoaulas.component.html',
  styleUrls: ['./videoaulas.component.css']
})
export class VideoaulasComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private cursoService = inject(CursoService);
  private progressoService = inject(ProgressoService);
  private sanitizer = inject(DomSanitizer);
  private snackBar = inject(MatSnackBar);
  private platformId = inject(PLATFORM_ID);

  curso: Curso | null = null;
  progresso: any = null;
  aulaAtual: Aula | null = null;
  urlVideoSegura: SafeResourceUrl | null = null;
  
  cursoId: number = 0;
  funcionarioId: number = 1; 

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const idParam = this.route.snapshot.paramMap.get('id');
      const userId = localStorage.getItem('usuarioId');
      
      if (idParam) {
        this.cursoId = +idParam;
        this.funcionarioId = userId ? +userId : 1;
        this.carregarDados();
      }
    }
  }

  carregarDados() {
    this.cursoService.obterPorId(this.cursoId).subscribe({
      next: (curso) => {
        this.curso = curso;
        this.buscarProgresso();
      },
      error: (err) => {
        console.error('Erro ao carregar curso:', err);
        this.snackBar.open('Erro ao carregar o curso.', 'Fechar');
      }
    });
  }

  buscarProgresso() {
    this.progressoService.obterProgressoEspecifico(this.funcionarioId, this.cursoId.toString()).subscribe({
      next: (progresso) => {
        this.progresso = progresso;
        this.determinarAulaAtual();
      },
      error: (err) => {
        console.error('Progresso não encontrado ou erro:', err);
        this.determinarAulaAtual(); 
      }
    });
  }

  determinarAulaAtual() {
    if (!this.curso || !this.curso.modulos) return;

    const aulasConcluidas = new Set(this.progresso?.aulasConcluidas || []);
    let encontrou = false;

    for (const modulo of this.curso.modulos) {
      if (!modulo.aulas) continue;

      for (const aula of modulo.aulas) {
        // Verifica se o ID existe antes de usar
        if (aula.id && !aulasConcluidas.has(aula.id.toString())) {
          this.selecionarAula(aula);
          encontrou = true;
          break;
        }
      }
      if (encontrou) break;
    }

    // Se não achou nenhuma pendente, pega a primeira
    if (!encontrou && this.curso.modulos.length > 0 && this.curso.modulos[0].aulas.length > 0) {
      this.selecionarAula(this.curso.modulos[0].aulas[0]);
    }
  }

  selecionarAula(aula: Aula) {
    this.aulaAtual = aula;
    this.urlVideoSegura = this.gerarUrlEmbedSegura(aula.urlConteudo);
    
    if (isPlatformBrowser(this.platformId)) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  private gerarUrlEmbedSegura(url: string): SafeResourceUrl {
    if (!url) return '';
    let videoId = '';

    if (url.includes('v=')) {
      videoId = url.split('v=')[1].split('&')[0];
    } else if (url.includes('youtu.be/')) {
       videoId = url.split('youtu.be/')[1];
    } else if (url.includes('/embed/')) {
       videoId = url.split('/embed/')[1];
    }

    if (!videoId) return this.sanitizer.bypassSecurityTrustResourceUrl(url);

    const embedUrl = `https://www.youtube.com/embed/${videoId}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }

  concluirAula() {
    if (!this.aulaAtual || !this.aulaAtual.id) return;

    const aulaIdStr = this.aulaAtual.id.toString();

    // Simulação visual
    this.snackBar.open(`Aula concluída!`, 'OK', { duration: 2000 });
    
    // Atualiza localmente
    if (this.progresso) {
        if (!this.progresso.aulasConcluidas) this.progresso.aulasConcluidas = [];
        this.progresso.aulasConcluidas.push(aulaIdStr);
    }
  }
  
  voltar() {
    this.router.navigate(['/meus-cursos']);
  }
  
  // Método auxiliar para o HTML
  isAulaConcluida(aula: Aula): boolean {
    if (!aula.id || !this.progresso?.aulasConcluidas) return false;
    return this.progresso.aulasConcluidas.includes(aula.id.toString());
  }
}