import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'; // Importar SnackBar

// Importar o Serviço
import { ProgressoService } from '../../../services/progresso.service';

@Component({
  selector: 'app-inscricao-curso',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatListModule,
    MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './inscricao-curso.component.html',
  styleUrls: ['./inscricao-curso.component.css']
})
export class InscricaoCursoComponent {

  curso: any;
  atendeRequisitos = false;
  inscrito = false;
  loading = false; // Para desabilitar botão enquanto carrega
  
  trilhaSugerida = [
    { titulo: 'Lógica de Programação', status: 'Pendente' },
    { titulo: 'Fundamentos de Java', status: 'Pendente' }
  ];

  constructor(
    private router: Router, 
    private location: Location,
    private progressoService: ProgressoService, // <--- Injetar Service
    private snackBar: MatSnackBar
  ) {
    const nav = this.router.getCurrentNavigation();
    if (nav?.extras.state && nav.extras.state['curso']) {
      this.curso = nav.extras.state['curso'];
    } else {
      // Mock de fallback
      this.curso = {
        id: 0, 
        titulo: "Curso Não Encontrado",
        descricao: "Volte ao catálogo.",
        xp: 0,
        duracao: 0,
        instrutor: "-",
        dificuldade: "-",
        prerequisitosAtendidos: false
      };
    }
    // Se tiver a flag ou se não tiver requisitos, libera
    this.atendeRequisitos = this.curso.prerequisitosAtendidos !== false;
  }

  inscrever() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
      this.snackBar.open('Erro: Usuário não identificado.', 'Fechar');
      return;
    }

    this.loading = true;

    // --- CHAMADA REAL AO BACKEND ---
    this.progressoService.matricular(Number(usuarioId), this.curso.id.toString())
      .subscribe({
        next: (res) => {
          console.log('Matrícula realizada:', res);
          this.inscrito = true;
          this.loading = false;
          
          // Redireciona após 1.5s
          setTimeout(() => {
            this.router.navigate(['/catalogo-cursos']); // Volta pro catálogo pra ver o botão mudar
          }, 1500);
        },
        error: (err) => {
          console.error('Erro ao matricular:', err);
          this.loading = false;
          this.snackBar.open('Erro ao realizar inscrição. Tente novamente.', 'Fechar');
        }
      });
  }

  voltar() {
    this.location.back();
  }
}