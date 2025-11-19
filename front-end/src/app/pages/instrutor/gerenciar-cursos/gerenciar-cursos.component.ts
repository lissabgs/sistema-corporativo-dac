import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

interface Course {
  id: number;
  titulo: string;
  codigo: string;
  ativo: boolean;
}

@Component({
  selector: 'app-gerenciar-cursos',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatDividerModule
  ],
  templateUrl: './gerenciar-cursos.component.html',
  styleUrls: ['./gerenciar-cursos.component.css']
})
export class GerenciarCursosComponent implements OnInit {
  // CORRIGIDO: Propriedade declarada no local correto
  cursos: Course[] = []; 

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Mock de dados: Lista de cursos do instrutor
    this.cursos = [
      { id: 1, titulo: 'Fundamentos de Angular', codigo: 'ANG-FND', ativo: true },
      { id: 2, titulo: 'Spring Boot Avançado', codigo: 'SB-ADV', ativo: true },
      { id: 3, titulo: 'Liderança Situacional', codigo: 'LDR-SIT', ativo: false }
    ];
  }

  editarCurso(cursoId: number) {
    this.router.navigate(['/cadastrar-curso'], { state: { cursoId: cursoId } });
  }

  excluirCurso(cursoId: number) {
    if (confirm('Tem certeza que deseja excluir este curso?')) {
      this.cursos = this.cursos.filter(c => c.id !== cursoId);
      console.log(`Curso ${cursoId} excluído.`);
    }
  }

  cadastrarNovoCurso() {
    this.router.navigate(['/cadastrar-curso']);
  }
}