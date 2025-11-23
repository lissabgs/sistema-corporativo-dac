import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

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
import { Curso } from '../../../models/curso.model';
import { DepartamentoService } from '../../../services/departamento.service';

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

  constructor(
    private fb: FormBuilder,
    private cursoService: CursoService,
    private departamentoService: DepartamentoService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.cursoForm = this.fb.group({
      // Validação: Padrão 3 letras, hífen, números (ex: DEV-001)
      codigo: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}-[0-9]+$/)]],
      titulo: ['', Validators.required],
      descricao: [''],
      categoriaId: ['', Validators.required], // Usaremos para o departamento
      instrutorId: [null, Validators.required], // Começa nulo, preenche no ngOnInit
      duracaoEstimada: ['', Validators.required],
      xpOferecido: [0, [Validators.required, Validators.min(0)]],
      nivelDificuldade: ['Iniciante', Validators.required],
      status: ['RASCUNHO', Validators.required],
      preRequisitos: [[]],
      modulos: this.fb.array([]) 
    });
  }

  ngOnInit(): void {
    this.carregarDepartamentos();

    // 1. Recupera o ID do usuário logado (Instrutor)
    const usuarioId = localStorage.getItem('usuarioId');
    if (usuarioId) {
      this.cursoForm.patchValue({ instrutorId: +usuarioId });
    } else {
      this.snackBar.open('Erro de sessão. Faça login novamente.', 'Fechar');
      // this.router.navigate(['/login']); // Opcional: Redirecionar se não tiver ID
    }

    // 2. Verifica se é edição
    const state = history.state;
    if (state && state.cursoId) {
      this.isEditMode = true;
      this.cursoId = state.cursoId;
      this.carregarCurso(this.cursoId!);
    }
  }

  carregarDepartamentos() {
    this.departamentoService.listarDepartamentos().subscribe(d => this.departamentos = d);
  }

  carregarCurso(id: number) {
    this.cursoService.obterPorId(id).subscribe({
      next: (curso) => {
        // Preenche o formulário básico
        this.cursoForm.patchValue(curso);
        
        // Ajusta a formatação da duração se necessário (remove 'h' se vier do back)
        if (curso.duracaoEstimada && typeof curso.duracaoEstimada === 'string') {
            const duracao = curso.duracaoEstimada.replace('h', '');
            this.cursoForm.patchValue({ duracaoEstimada: duracao });
        }

        // Limpa e recria os módulos (FormArray)
        this.modulos.clear();
        if (curso.modulos) {
          curso.modulos.forEach(mod => {
            const modGroup = this.criarModuloGroup(mod);
            this.modulos.push(modGroup);
          });
        }
      },
      error: () => this.snackBar.open('Erro ao carregar curso.', 'Fechar')
    });
  }

  // --- Lógica de Formatação do Código ---
  formatarCodigo(event: any) {
    const input = event.target as HTMLInputElement;
    // Remove tudo que não é letra ou número e deixa maiúsculo
    let valor = input.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
    
    // Pega os primeiros 3 caracteres (devem ser letras)
    let letras = valor.substring(0, 3).replace(/[^A-Z]/g, '');
    
    // Pega o restante (devem ser números)
    let numeros = valor.substring(3).replace(/[^0-9]/g, '');

    // Monta a máscara: XXX-XXXX...
    let final = letras;
    if (letras.length === 3) {
      if (numeros.length > 0) {
        final += '-' + numeros;
      }
    }

    // Atualiza o valor no input e no formControl
    input.value = final;
    this.cursoForm.get('codigo')?.setValue(final, { emitEvent: false });
  }

  // --- Lógica de UX do campo XP e Numéricos ---
  
  // Bloqueia caracteres não numéricos (e, E, +, -)
  bloquearCaracteresNaoNumericos(event: KeyboardEvent) {
    const teclasProibidas = ['e', 'E', '+', '-'];
    if (teclasProibidas.includes(event.key)) {
      event.preventDefault();
    }
  }

  limparSeZero(campo: string) {
    const valor = this.cursoForm.get(campo)?.value;
    if (valor === 0) {
      this.cursoForm.get(campo)?.setValue(null);
    }
  }

  restaurarSeVazio(campo: string) {
    const valor = this.cursoForm.get(campo)?.value;
    if (valor === null || valor === '') {
      this.cursoForm.get(campo)?.setValue(0);
    }
  }

  // --- Helpers de Formulário ---
  get modulos(): FormArray {
    return this.cursoForm.get('modulos') as FormArray;
  }

  aulas(index: number): FormArray {
    return this.modulos.at(index).get('aulas') as FormArray;
  }

  criarModuloGroup(dados?: any): FormGroup {
    const grupo = this.fb.group({
      titulo: [dados?.titulo || 'Novo Módulo', Validators.required],
      ordem: [dados?.ordem || this.modulos.length + 1, Validators.required],
      aulas: this.fb.array([])
    });

    if (dados?.aulas) {
      const aulasArray = grupo.get('aulas') as FormArray;
      dados.aulas.forEach((aula: any) => {
        aulasArray.push(this.fb.group({
          titulo: [aula.titulo, Validators.required],
          urlConteudo: [aula.urlConteudo, Validators.required],
          ordem: [aula.ordem],
          obrigatorio: [aula.obrigatorio]
          // REMOVIDO: xpModulo: [aula.xpModulo]
        }));
      });
    }
    return grupo;
  }

  adicionarModulo() {
    this.modulos.push(this.criarModuloGroup());
  }

  removerModulo(index: number) {
    this.modulos.removeAt(index);
  }

  adicionarAula(modIndex: number) {
    const aulas = this.aulas(modIndex);
    aulas.push(this.fb.group({
      titulo: ['', Validators.required],
      urlConteudo: ['', Validators.required],
      ordem: [aulas.length + 1],
      obrigatorio: [true]
      // REMOVIDO: xpModulo: [10, Validators.min(0)]
    }));
  }

  removerAula(modIndex: number, aulaIndex: number) {
    this.aulas(modIndex).removeAt(aulaIndex);
  }

  onSubmit() {
    if (this.cursoForm.invalid) {
      this.cursoForm.markAllAsTouched();
      
      // Log para debug
      console.log('Formulário inválido:', this.cursoForm.errors);
      Object.keys(this.cursoForm.controls).forEach(key => {
        const controlErrors = this.cursoForm.get(key)?.errors;
        if (controlErrors) {
          console.log('Erro no campo ' + key + ':', controlErrors);
        }
      });

      this.snackBar.open('Verifique os campos obrigatórios (ex: Código XXX-123)', 'Fechar', { duration: 3000 });
      return;
    }

    // Pega valores mesmo se disabled e garante conversão de tipos
    const formValue = this.cursoForm.getRawValue();
    
    const cursoData = {
        ...formValue,
        // Garante que XP seja número
        xpOferecido: Number(formValue.xpOferecido),
        // Formata a duração para string "XXh" se necessário pelo backend, ou mantém número se ele aceitar
        duracaoEstimada: formValue.duracaoEstimada.toString() + 'h' 
    };

    if (this.isEditMode && this.cursoId) {
      this.cursoService.atualizar(this.cursoId, cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso atualizado!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao atualizar curso.', 'Fechar');
        }
      });
    } else {
      this.cursoService.criar(cursoData).subscribe({
        next: () => {
          this.snackBar.open('Curso criado!', 'OK', { duration: 3000 });
          this.router.navigate(['/gerenciar-cursos']);
        },
        error: (err) => {
          console.error(err);
          this.snackBar.open('Erro ao criar curso.', 'Fechar');
        }
      });
    }
  }

  voltar() {
    this.router.navigate(['/gerenciar-cursos']);
  }
}