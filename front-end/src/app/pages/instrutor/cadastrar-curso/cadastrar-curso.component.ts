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
  
  departamentos: any[] = [];
  todosStatus = ['RASCUNHO', 'ATIVO', 'INATIVO', 'PAUSADO', 'ARQUIVADO'];

  constructor(
    private fb: FormBuilder,
    private cursoService: CursoService,
    private departamentoService: DepartamentoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.cursoForm = this.fb.group({
      codigo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: [''],
      categoriaId: ['', Validators.required],
      instrutorId: [null, Validators.required],
      duracaoEstimada: ['', [Validators.required, Validators.min(1)]],
      xpOferecido: [0, [Validators.required, Validators.min(0)]],
      nivelDificuldade: ['Iniciante', Validators.required],
      status: ['RASCUNHO', Validators.required],
      preRequisitos: [[]],
      modulos: this.fb.array([]) 
    });
  }

  ngOnInit(): void {
    this.carregarDepartamentos();

    // Tenta recuperar o ID salvo no login
    const usuarioId = localStorage.getItem('usuarioId');

    if (usuarioId) {
      this.cursoForm.patchValue({ instrutorId: +usuarioId });
    } else {
      console.error('ERRO CRÍTICO: ID do usuário não encontrado no LocalStorage!');
      
      this.snackBar.open('Sessão inválida. Por favor, faça login novamente.', 'Ok');
      this.router.navigate(['/login']);
    }

    const state = history.state;
    if (state && state.cursoId) {
      this.isEditMode = true;
      this.cursoId = state.cursoId;
    }
  }

  carregarDepartamentos() {
    this.departamentoService.listarDepartamentos().subscribe({
      next: (data) => this.departamentos = data,
      error: () => this.snackBar.open('Erro ao carregar departamentos', 'Fechar')
    });
  }

  bloquearLetras(event: any) {
    const input = event.target;
    // Substitui tudo que não é número por vazio
    input.value = input.value.replace(/[^0-9]/g, '');
    // Atualiza o form control para refletir a mudança
    this.cursoForm.get(input.getAttribute('formControlName'))?.setValue(input.value);
  }

  // Formata o Código: XXX-0000
  formatarCodigo(event: any) {
    let valor = event.target.value.toUpperCase();
    
    // Remove caracteres inválidos (mantém apenas letras e números)
    let limpo = valor.replace(/[^A-Z0-9]/g, '');

    // Garante que os 3 primeiros sejam letras
    let prefixo = limpo.substring(0, 3).replace(/[^A-Z]/g, '');
    
    // O restante devem ser números (máximo 4)
    let sufixo = limpo.substring(3).replace(/[^0-9]/g, '').substring(0, 4);

    // Monta o código final
    let codigoFormatado = prefixo;
    if (sufixo.length > 0) {
      codigoFormatado += '-' + sufixo;
    }

    // Atualiza o input
    this.cursoForm.get('codigo')?.setValue(codigoFormatado, { emitEvent: false });
  }


  get opcoesStatusDisponiveis(): string[] {
    const atual = this.cursoForm.get('status')?.value;
    if (!this.isEditMode && !atual) return ['RASCUNHO'];

    switch (atual) {
      case 'RASCUNHO': return ['RASCUNHO', 'ATIVO', 'ARQUIVADO'];
      case 'ATIVO': return ['ATIVO', 'INATIVO'];
      case 'INATIVO': return ['INATIVO', 'ATIVO'];
      case 'ARQUIVADO': return ['ARQUIVADO', 'PAUSADO', 'RASCUNHO'];
      case 'PAUSADO': return ['PAUSADO', 'ARQUIVADO'];
      default: return ['RASCUNHO'];
    }
  }

  // --- GETTERS E MÉTODOS DE ARRAY (Mantidos) ---
  get modulos(): FormArray {
    return this.cursoForm.get('modulos') as FormArray;
  }

  aulas(moduloIndex: number): FormArray {
    return this.modulos.at(moduloIndex).get('aulas') as FormArray;
  }

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