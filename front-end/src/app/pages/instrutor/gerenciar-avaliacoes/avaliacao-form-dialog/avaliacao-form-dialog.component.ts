import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs'; // IMPORTANTE: Para requisições paralelas

// Services e Models
import { AvaliacaoService } from '../../../../services/avaliacao.service';
import { CursoService } from '../../../../services/curso.service'; // Importe seu CursoService
import { Avaliacao } from '../../../../models/avaliacao.model';
import { Curso } from '../../../../models/curso.model'; // Certifique-se de ter este model

@Component({
  selector: 'app-avaliacao-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatDividerModule,
    MatExpansionModule,
    MatSnackBarModule
  ],
  templateUrl: './avaliacao-form-dialog.component.html',
  styleUrls: ['./avaliacao-form-dialog.component.css']
})
export class AvaliacaoFormDialogComponent implements OnInit {
  form: FormGroup;
  isEditMode: boolean = false;
  
  // Listas para controle
  cursosDisponiveis: Curso[] = [];
  todosCursos: Curso[] = []; // Cópia de segurança para recuperar dados

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    private cursoService: CursoService, // Injeção do CursoService
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<AvaliacaoFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { avaliacao?: Avaliacao }
  ) {
    this.form = this.fb.group({
      id: [null],
      codigo: ['', Validators.required], // Mantemos no form, mas será oculto/automático
      titulo: ['', Validators.required],
      descricao: [''],
      cursoId: [null, Validators.required],
      tempoLimiteMinutos: [60, [Validators.required, Validators.min(1)]],
      tentativasPermitidas: [3, [Validators.required, Validators.min(1)]],
      notaMinima: [70.0, [Validators.required, Validators.min(0), Validators.max(100)]],
      questoes: this.fb.array([])
    });
  }

// ... imports

  ngOnInit(): void {
    if (this.data?.avaliacao) {
      this.isEditMode = true;
      // No modo edição, buscamos apenas o curso dessa avaliação específica para exibir
      // ou carregamos todos e filtramos conforme necessário. 
      // Para simplificar, vou manter o preenchimento direto:
      this.cursosDisponiveis = []; // Em edição geralmente não troca o curso
      this.preencherFormulario(this.data.avaliacao);
    } else {
      // MODO CRIAÇÃO: Busca lista filtrada do Backend
      this.carregarCursosDisponiveis();
    }
  }

  carregarCursosDisponiveis() {
    this.avaliacaoService.listarCursosDisponiveis().subscribe({
      next: (cursos) => {
        this.cursosDisponiveis = cursos;
        
        if (this.cursosDisponiveis.length === 0) {
          this.snackBar.open('Nenhum curso disponível para criar avaliação (todos já possuem prova ou não há cursos).', 'OK', {
            duration: 5000,
            panelClass: ['warning-snackbar'] 
          });
        }
      },
      error: (err) => {
        console.error(err);
        if (err.status === 0) {
          this.snackBar.open('Erro: Não foi possível conectar ao servidor. Verifique se o Backend está rodando.', 'Fechar', {
            panelClass: ['error-snackbar']
          });
        } else {
          this.snackBar.open('Erro ao carregar cursos disponíveis.', 'Fechar');
        }
      }
    });
  }

  carregarDadosIniciais() {
    forkJoin({
      cursos: this.cursoService.listarCursos(),
      avaliacoes: this.avaliacaoService.listar()
    }).subscribe({
      next: (response) => {
        this.todosCursos = response.cursos;
        const avaliacoesExistentes = response.avaliacoes;

        if (this.data?.avaliacao) {
          // MODO EDIÇÃO
          this.isEditMode = true;
          
          // No modo edição, precisamos permitir o curso atual na lista, mesmo que já tenha avaliação (ela mesma)
          const idCursoAtual = this.data.avaliacao.cursoId;
          
          this.cursosDisponiveis = this.todosCursos.filter(c => 
            c.id === idCursoAtual || !avaliacoesExistentes.some(a => a.cursoId === c.id)
          );

          this.preencherFormulario(this.data.avaliacao);

        } else {
          // MODO CRIAÇÃO
          // Filtra: Traz cursos ONDE NÃO EXISTE uma avaliação com aquele cursoId
          this.cursosDisponiveis = this.todosCursos.filter(c => 
            !avaliacoesExistentes.some(a => a.cursoId === c.id)
          );
        }
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Erro ao carregar cursos.', 'Fechar');
      }
    });
  }

  // Evento disparado ao selecionar um curso na Combo
  aoSelecionarCurso(cursoId: number) {
    const cursoSelecionado = this.todosCursos.find(c => c.id === cursoId);
    if (cursoSelecionado) {
      // Regra: O código da avaliação será igual ao código do curso
      this.form.patchValue({
        codigo: cursoSelecionado.codigo,
        titulo: `Avaliação Final - ${cursoSelecionado.titulo}` // Sugestão: já preenche o título também
      });
    }
  }

  preencherFormulario(avaliacao: Avaliacao) {
    this.form.patchValue({
      id: avaliacao.id,
      codigo: avaliacao.codigo,
      titulo: avaliacao.titulo,
      descricao: avaliacao.descricao,
      cursoId: avaliacao.cursoId,
      tempoLimiteMinutos: avaliacao.tempoLimiteMinutos,
      tentativasPermitidas: avaliacao.tentativasPermitidas,
      notaMinima: avaliacao.notaMinima
    });

    if (avaliacao.questoes) {
      avaliacao.questoes.forEach(q => {
        this.questoes.push(this.criarQuestaoGroup(q));
      });
    }
  }

  // ... (Restante dos métodos: questoes, criarQuestaoGroup, adicionarQuestao, removerQuestao, onSubmit mantêm-se iguais)
  
  get questoes(): FormArray {
    return this.form.get('questoes') as FormArray;
  }

  criarQuestaoGroup(dados?: any): FormGroup {
    return this.fb.group({
      id: [dados?.id || null],
      tipoQuestao: [dados?.tipoQuestao || 'OBJETIVA', Validators.required],
      enunciado: [dados?.enunciado || '', Validators.required],
      opcoesResposta: [dados?.opcoesResposta || ''],
      respostaCorreta: [dados?.respostaCorreta || '', Validators.required],
      peso: [dados?.peso || 1, [Validators.required, Validators.min(0)]],
      ordem: [dados?.ordem || this.questoes.length + 1]
    });
  }

  adicionarQuestao() {
    this.questoes.push(this.criarQuestaoGroup());
  }

  removerQuestao(index: number) {
    this.questoes.removeAt(index);
  }

  onSubmit() {
    if (this.form.invalid) return;
    const avaliacao = this.form.getRawValue();

    if (this.isEditMode && avaliacao.id) {
      this.avaliacaoService.atualizar(avaliacao.id, avaliacao).subscribe({
        next: () => {
          this.snackBar.open('Avaliação atualizada!', 'OK', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => this.snackBar.open('Erro ao atualizar.', 'Fechar')
      });
    } else {
      this.avaliacaoService.criar(avaliacao).subscribe({
        next: () => {
          this.snackBar.open('Avaliação criada!', 'OK', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => this.snackBar.open('Erro ao criar.', 'Fechar')
      });
    }
  }
}