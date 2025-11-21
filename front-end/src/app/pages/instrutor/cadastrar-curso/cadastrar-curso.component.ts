import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';

import { CursoService } from '../../../services/curso.service';
import { Curso, Modulo, Aula } from '../../../models/curso.model';

@Component({
  selector: 'app-cadastrar-curso',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    MatExpansionModule,
    MatCheckboxModule,
    MatSnackBarModule,
    MatDividerModule
  ],
  templateUrl: './cadastrar-curso.component.html',
  styleUrls: ['./cadastrar-curso.component.css']
})
export class CadastrarCursoComponent implements OnInit {
  cursoForm: FormGroup;
  isEditMode = false;
  cursoId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private cursoService: CursoService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.cursoForm = this.fb.group({
      codigo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: [''],
      categoriaId: ['', Validators.required],
      instrutorId: [1], // TODO: Pegar do token/localStorage
      duracaoEstimada: ['', Validators.required],
      xpOferecido: [0, [Validators.required, Validators.min(0)]],
      nivelDificuldade: ['Iniciante', Validators.required],
      status: ['RASCUNHO', Validators.required],
      preRequisitos: [[]], // Simplificado para demo
      modulos: this.fb.array([]) 
    });
  }

  ngOnInit(): void {
    // Verifica se veio ID no state (navegação interna) ou na rota
    const state = history.state;
    if (state && state.cursoId) {
      this.cursoId = state.cursoId;
      this.isEditMode = true;
      // TODO: Chamar this.carregarCurso(this.cursoId);
    }
  }

  // --- GETTERS ---
  get modulos(): FormArray {
    return this.cursoForm.get('modulos') as FormArray;
  }

  aulas(moduloIndex: number): FormArray {
    return this.modulos.at(moduloIndex).get('aulas') as FormArray;
  }

  // --- MÉTODOS DE MÓDULO ---
  adicionarModulo() {
    const moduloGroup = this.fb.group({
      titulo: ['Novo Módulo', Validators.required],
      ordem: [this.modulos.length + 1, Validators.required],
      aulas: this.fb.array([]) 
    });
    this.modulos.push(moduloGroup);
  }

  removerModulo(index: number) {
    this.modulos.removeAt(index);
  }

  // --- MÉTODOS DE AULA ---
  adicionarAula(moduloIndex: number) {
    const aulasArray = this.aulas(moduloIndex);
    const aulaGroup = this.fb.group({
      titulo: ['', Validators.required],
      descricao: [''],
      urlConteudo: ['', Validators.required],
      ordem: [aulasArray.length + 1],
      obrigatorio: [true],
      xpModulo: [10, Validators.min(0)]
    });
    aulasArray.push(aulaGroup);
  }

  removerAula(moduloIndex: number, aulaIndex: number) {
    this.aulas(moduloIndex).removeAt(aulaIndex);
  }

  // --- SALVAR ---
  onSubmit() {
    if (this.cursoForm.invalid) {
      this.cursoForm.markAllAsTouched();
      this.snackBar.open('Verifique os campos obrigatórios', 'Fechar', { duration: 3000 });
      return;
    }

    const cursoData: Curso = this.cursoForm.value;

    // Se for edição
    if (this.isEditMode && this.cursoId) {
      this.cursoService.atualizar(this.cursoId, cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso atualizado com sucesso!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao atualizar.', 'Fechar');
        }
      });
    } else {
      // Se for criação
      this.cursoService.criar(cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso criado com sucesso!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao criar.', 'Fechar');
        }
      });
    }
  }

  voltar() {
    this.router.navigate(['/gerenciar-cursos']);
  }
}