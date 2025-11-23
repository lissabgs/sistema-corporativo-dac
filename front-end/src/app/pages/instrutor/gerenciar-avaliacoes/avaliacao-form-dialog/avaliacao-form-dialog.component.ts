import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { AvaliacaoService } from '../../../../services/avaliacao.service';
import { Avaliacao } from '../../../../models/avaliacao.model';

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
    MatRadioModule,
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
  cursosDisponiveis: any[] = [];

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<AvaliacaoFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { avaliacao?: Avaliacao }
  ) {
    this.form = this.fb.group({
      id: [null],
      // O código e título serão preenchidos ao selecionar o curso
      codigo: ['', Validators.required], 
      titulo: ['', Validators.required],
      descricao: [''],
      // Novo campo global para definir o tipo da prova inteira
      tipoAvaliacao: ['OBJETIVA', Validators.required], 
      cursoId: [null, Validators.required],
      tempoLimiteMinutos: [60, [Validators.required, Validators.min(1)]],
      tentativasPermitidas: [3, [Validators.required, Validators.min(1)]],
      notaMinima: [70.0, [Validators.required, Validators.min(0), Validators.max(100)]],
      questoes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.carregarCursos();

    // Se o usuário trocar o tipo de prova, limpamos as questões para evitar erros
    this.form.get('tipoAvaliacao')?.valueChanges.subscribe(() => {
      this.questoes.clear();
    });
  }

  carregarCursos() {
    this.avaliacaoService.listarCursosDisponiveis().subscribe({
      next: (cursos) => {
        this.cursosDisponiveis = cursos;
        if (this.data?.avaliacao) {
          this.isEditMode = true;
          this.preencherDados(this.data.avaliacao);
        }
      },
      error: () => this.snackBar.open('Erro ao carregar cursos.', 'Fechar')
    });
  }

  aoSelecionarCurso(cursoId: number) {
    const curso = this.cursosDisponiveis.find(c => c.id === cursoId);
    if (curso) {
      this.form.patchValue({
        codigo: curso.codigo, // Código do curso vira código da avaliação
        titulo: `Avaliação Final - ${curso.titulo}`
      });
    }
  }

  preencherDados(avaliacao: Avaliacao) {
    // Preenche dados básicos
    this.form.patchValue(avaliacao);
    
    // Detecta o tipo da primeira questão para definir o tipo da prova (se houver questões)
    if (avaliacao.questoes && avaliacao.questoes.length > 0) {
      const tipo = avaliacao.questoes[0].tipoQuestao;
      this.form.patchValue({ tipoAvaliacao: tipo });
      
      // Recria o FormArray de questões
      avaliacao.questoes.forEach(q => {
        const group = this.criarQuestaoGroup();
        
        // Se for objetiva, precisamos "explodir" o JSON de volta para os campos A, B, C...
        if (tipo === 'OBJETIVA' && q.opcoesResposta) {
          try {
            const opcoes = JSON.parse(q.opcoesResposta);
            group.patchValue({
              alternativaA: opcoes.A,
              alternativaB: opcoes.B,
              alternativaC: opcoes.C,
              alternativaD: opcoes.D,
              alternativaE: opcoes.E,
              respostaCorretaSelecionada: q.respostaCorreta
            });
          } catch (e) {
            console.error('Erro ao parsear opções', e);
          }
        }
        
        group.patchValue({
          id: q.id,
          enunciado: q.enunciado,
          peso: q.peso
        });
        
        this.questoes.push(group);
      });
    }
  }

  get questoes(): FormArray {
    return this.form.get('questoes') as FormArray;
  }

  criarQuestaoGroup(): FormGroup {
    const tipo = this.form.get('tipoAvaliacao')?.value;

    const group = this.fb.group({
      id: [null],
      enunciado: ['', Validators.required],
      peso: [1, [Validators.required, Validators.min(0.1)]],
      
      // Campos visuais para as alternativas (usados apenas no front)
      alternativaA: [''],
      alternativaB: [''],
      alternativaC: [''],
      alternativaD: [''],
      alternativaE: [''],
      respostaCorretaSelecionada: [''], // Armazena "A", "B", etc.
      
      // Campos ocultos que serão enviados ao backend
      tipoQuestao: [tipo], 
      opcoesResposta: [''], 
      respostaCorreta: ['']
    });

    // Validação condicional: Se for objetiva, obriga preencher alternativas e selecionar a correta
    if (tipo === 'OBJETIVA') {
      group.get('alternativaA')?.setValidators(Validators.required);
      group.get('alternativaB')?.setValidators(Validators.required);
      group.get('respostaCorretaSelecionada')?.setValidators(Validators.required);
    }

    return group;
  }

  adicionarQuestao() {
    this.questoes.push(this.criarQuestaoGroup());
  }

  removerQuestao(index: number) {
    this.questoes.removeAt(index);
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.getRawValue();
    const tipoGlobal = formValue.tipoAvaliacao;

    // PREPARAÇÃO DOS DADOS PARA O BACKEND
    formValue.questoes = formValue.questoes.map((q: any) => {
      
      q.tipoQuestao = tipoGlobal; // Garante que todas seguem o tipo da prova

      if (tipoGlobal === 'OBJETIVA') {
        // Monta o JSON das opções: {"A": "Texto...", "B": "Texto..."}
        const opcoesObj = {
          A: q.alternativaA,
          B: q.alternativaB,
          C: q.alternativaC,
          D: q.alternativaD,
          E: q.alternativaE
        };
        q.opcoesResposta = JSON.stringify(opcoesObj);
        q.respostaCorreta = q.respostaCorretaSelecionada; // Ex: "C"

      } else {
        // Discursiva: campos de resposta ficam nulos
        q.opcoesResposta = null;
        q.respostaCorreta = null; 
      }

      // Remove campos temporários do front para não enviar lixo
      delete q.alternativaA; delete q.alternativaB; delete q.alternativaC;
      delete q.alternativaD; delete q.alternativaE; delete q.respostaCorretaSelecionada;

      return q;
    });

    // Envio (Create ou Update)
    const request$ = this.isEditMode 
      ? this.avaliacaoService.atualizar(formValue.id, formValue)
      : this.avaliacaoService.criar(formValue);

    request$.subscribe({
      next: () => {
        this.snackBar.open('Avaliação salva com sucesso!', 'OK', { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Erro ao salvar. Verifique os campos.', 'Fechar');
      }
    });
  }
}