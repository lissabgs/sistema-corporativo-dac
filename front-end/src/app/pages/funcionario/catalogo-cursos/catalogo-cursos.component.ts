import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Angular Material
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-catalogo-cursos',
  standalone: true,
  templateUrl: './catalogo-cursos.component.html',
  styleUrls: ['./catalogo-cursos.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule
  ]
})
export class CatalogoCursosComponent {

  // Lista de cursos mockada diretamente aqui
  cursos = [
    {
      id: 1,
      titulo: 'Introdução ao Angular',
      descricao: 'Aprenda os fundamentos do Angular.',
      duracao: 8,
      categoria: 'Tecnologia',
      instrutor: 'Maria Oliveira',
      dificuldade: 'Iniciante',
      avaliacaoMedia: 4.7,
      xp: 120,
      prerequisitosAtendidos: true
    },
    {
      id: 2,
      titulo: 'Spring Boot e Microsserviços',
      descricao: 'Crie sistemas avançados com arquitetura moderna.',
      duracao: 14,
      categoria: 'Backend',
      instrutor: 'Carlos Mendes',
      dificuldade: 'Avançado',
      avaliacaoMedia: 4.9,
      xp: 250,
      prerequisitosAtendidos: false
    }
  ];

  cursosFiltrados = [...this.cursos];

  // Filtros
  search = '';
  categoria = '';
  instrutor = '';
  dificuldade = '';
  avaliacaoMin = 0;

  categorias = ['Tecnologia', 'Backend', 'Soft Skills', 'Liderança'];
  instrutores = ['Maria Oliveira', 'Carlos Mendes', 'João Silva'];
  dificuldades = ['Iniciante', 'Intermediário', 'Avançado'];

  filtrar() {
    this.cursosFiltrados = this.cursos.filter(c =>
      (this.search === '' || c.titulo.toLowerCase().includes(this.search.toLowerCase())) &&
      (this.categoria === '' || c.categoria === this.categoria) &&
      (this.instrutor === '' || c.instrutor === this.instrutor) &&
      (this.dificuldade === '' || c.dificuldade === this.dificuldade) &&
      (c.avaliacaoMedia >= this.avaliacaoMin)
    );
  }

  limparFiltros() {
    this.search = '';
    this.categoria = '';
    this.instrutor = '';
    this.dificuldade = '';
    this.avaliacaoMin = 0;
    this.cursosFiltrados = [...this.cursos];
  }
}
