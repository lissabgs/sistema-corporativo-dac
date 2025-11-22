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

// Serviços e Models
import { CursoService } from '../../../services/curso.service';
import { DepartamentoService } from '../../../services/departamento.service';
import { Curso } from '../../../models/curso.model';

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
  
  // Lista de departamentos (para o select)
  departamentos: any[] = [];

  // Status possíveis
  todosStatus = ['RASCUNHO', 'ATIVO', 'INATIVO', 'PAUSADO', 'ARQUIVADO'];

  constructor(
    private fb: FormBuilder,
    private cursoService: CursoService,
    private departamentoService: DepartamentoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    // CORREÇÃO: Recupera o ID do usuário logado (Instrutor) do localStorage
    const usuarioId = Number(localStorage.getItem('usuarioId'));

    this.cursoForm = this.fb.group({
      codigo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: [''],
      categoriaId: ['', Validators.required],
      // Usa o ID recuperado. Se não houver (erro de login), fica null e o Validator barra.
      instrutorId: [usuarioId || null, Validators.required], 
      duracaoEstimada: ['', [Validators.required, Validators.min(1)]],
      xpOferecido: [0, [Validators.required, Validators.min(0)]],
      nivelDificuldade: ['Iniciante', Validators.required],
      status: ['RASCUNHO', Validators.required],
      preRequisitos: [[]],
      modulos: this.fb.array([]) 
    });
  }

  ngOnInit(): void {
    // 1. Carregar Departamentos
    this.carregarDepartamentos();

    // 2. Verificar Edição
    const state = history.state;
    if (state && state.cursoId) {
      this.cursoId = state.cursoId;
      this.isEditMode = true;
      // Se fosse edição real, aqui carregaríamos os dados do curso do backend
      // this.carregarCurso(this.cursoId);
    }
  }

  carregarDepartamentos() {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (data) => this.departamentos = data,
      error: () => this.snackBar.open('Erro ao carregar departamentos', 'Fechar')
    });
  }

  // --- LÓGICA DE STATUS (Regras do Usuário) ---
  get opcoesStatusDisponiveis(): string[] {
    const atual = this.cursoForm.get('status')?.value;

    if (!this.isEditMode && !atual) return ['RASCUNHO'];

    switch (atual) {
      case 'RASCUNHO':
        return ['RASCUNHO', 'ATIVO', 'ARQUIVADO'];
      case 'ATIVO':
        return ['ATIVO', 'INATIVO'];
      case 'INATIVO':
        return ['INATIVO', 'ATIVO'];
      case 'ARQUIVADO':
        return ['ARQUIVADO', 'PAUSADO', 'RASCUNHO'];
      case 'PAUSADO':
        return ['PAUSADO', 'ARQUIVADO'];
      default:
        return ['RASCUNHO'];
    }
  }

  // --- GETTERS ---
  get modulos(): FormArray {
    return this.cursoForm.get('modulos') as FormArray;
  }

  aulas(moduloIndex: number): FormArray {
    return this.modulos.at(moduloIndex).get('aulas') as FormArray;
  }

  // --- MÉTODOS DE FORMULÁRIO (Adicionar/Remover) ---
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

  adicionarAula(moduloIndex: number) {
    const aulasArray = this.aulas(moduloIndex);
    const aulaGroup = this.fb.group({
      titulo: ['', Validators.required],
      urlConteudo: ['', Validators.required],
      xpModulo: [10, [Validators.required, Validators.min(0)]], 
      obrigatorio: [true]
    });
    aulasArray.push(aulaGroup);
  }

  removerAula(moduloIndex: number, aulaIndex: number) {
    this.aulas(moduloIndex).removeAt(aulaIndex);
  }

  onSubmit() {
    if (this.cursoForm.invalid) {
      this.cursoForm.markAllAsTouched();
      this.snackBar.open('Verifique os campos inválidos', 'Fechar', { duration: 3000 });
      return;
    }

    const formValue = this.cursoForm.value;
    const cursoData = {
      ...formValue,
      duracaoEstimada: formValue.duracaoEstimada.toString() + 'h' 
    };

    if (this.isEditMode && this.cursoId) {
      this.cursoService.atualizar(this.cursoId, cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso atualizado!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: () => this.snackBar.open('Erro ao atualizar.', 'Fechar')
      });
    } else {
      this.cursoService.criar(cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso criado!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: () => this.snackBar.open('Erro ao criar.', 'Fechar')
      });
    }
  }

  voltar() {
    this.router.navigate(['/gerenciar-cursos']);
  }
}