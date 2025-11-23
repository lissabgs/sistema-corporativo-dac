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
      codigo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: [''],
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

    // Limpa questões se trocar o tipo de prova para evitar inconsistências
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
        codigo: curso.codigo,
        titulo: `Avaliação - ${curso.titulo}`
      });
    }
  }

  preencherDados(avaliacao: Avaliacao) {
    this.form.patchValue(avaliacao);
    
    if (avaliacao.questoes && avaliacao.questoes.length > 0) {
      // Define o tipo com base na primeira questão
      const tipo = avaliacao.questoes[0].tipoQuestao;
      this.form.patchValue({ tipoAvaliacao: tipo });
      
      avaliacao.questoes.forEach(q => {
        const group = this.criarQuestaoGroup();
        
        // Se for objetiva, faz o parse do JSON de volta para os campos A, B, C...
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
          } catch (e) { console.error('Erro ao ler opções', e); }
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
      
      // Campos Visuais (Alternativas)
      alternativaA: [''],
      alternativaB: [''],
      alternativaC: [''],
      alternativaD: [''],
      alternativaE: [''],
      respostaCorretaSelecionada: [''],
      
      // Campos Ocultos (para o Backend)
      tipoQuestao: [tipo], 
      opcoesResposta: [''], 
      respostaCorreta: ['']
    });

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

    // --- AQUI ESTÁ A CORREÇÃO CRUCIAL ---
    // Transforma os dados do formulário no formato que o Java espera (QuestaoDTO)
    formValue.questoes = formValue.questoes.map((q: any, index: number) => {
      
      q.tipoQuestao = tipoGlobal;
      q.ordem = index + 1; // Define a ordem sequencial (1, 2, 3...)

      if (tipoGlobal === 'OBJETIVA') {
        // Cria o JSON das opções
        const opcoesObj = {
          A: q.alternativaA,
          B: q.alternativaB,
          C: q.alternativaC,
          D: q.alternativaD,
          E: q.alternativaE
        };
        q.opcoesResposta = JSON.stringify(opcoesObj); // Campo que o Java lê
        q.respostaCorreta = q.respostaCorretaSelecionada; // Campo que o Java lê
      } else {
        q.opcoesResposta = null;
        q.respostaCorreta = null; 
      }

      // Limpa o objeto para não enviar lixo
      delete q.alternativaA; delete q.alternativaB; delete q.alternativaC;
      delete q.alternativaD; delete q.alternativaE; delete q.respostaCorretaSelecionada;

      return q;
    });

    // Envio
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
        this.snackBar.open('Erro ao salvar. Verifique o console.', 'Fechar');
      }
    });
  }
}