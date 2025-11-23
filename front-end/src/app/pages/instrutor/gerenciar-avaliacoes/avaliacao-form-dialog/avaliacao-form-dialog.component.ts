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
import { CursoService } from '../../../../services/curso.service'; // <--- Importe o CursoService
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
    private cursoService: CursoService, // <--- Injete aqui
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
    // Verifica se é edição ou criação
    if (this.data?.avaliacao) {
      this.isEditMode = true;
 
      this.form.get('cursoId')?.disable(); 

      const cursoIdAtual = this.data.avaliacao.cursoId;
      if (cursoIdAtual) {
        this.cursoService.obterPorId(cursoIdAtual).subscribe({
          next: (curso) => {
            this.cursosDisponiveis = [curso]; 
            this.preencherDados(this.data.avaliacao!);
          },
          error: () => this.snackBar.open('Erro ao carregar dados do curso.', 'Fechar')
        });
      }
    } else {

      this.carregarCursosDisponiveis();
    }

    // Listener para limpar questões se trocar o tipo
    this.form.get('tipoAvaliacao')?.valueChanges.subscribe(() => {
      this.questoes.clear();
    });
  }

  carregarCursosDisponiveis() {
    this.avaliacaoService.listarCursosDisponiveis().subscribe({
      next: (cursos) => {
        this.cursosDisponiveis = cursos;
      },
      error: () => this.snackBar.open('Erro ao carregar cursos disponíveis.', 'Fechar')
    });
  }

  aoSelecionarCurso(cursoId: number) {
    const curso = this.cursosDisponiveis.find(c => c.id === cursoId);
    if (curso) {
      this.form.patchValue({
        codigo: curso.codigo,
        titulo: `Avaliação Final - ${curso.titulo}`
      });
    }
  }

  preencherDados(avaliacao: Avaliacao) {
    this.form.patchValue(avaliacao);
    
    if (avaliacao.questoes && avaliacao.questoes.length > 0) {
      const tipo = avaliacao.questoes[0].tipoQuestao;
      this.form.patchValue({ tipoAvaliacao: tipo });
      
      avaliacao.questoes.forEach(q => {
        const group = this.criarQuestaoGroup();
        
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
          } catch (e) { console.error('Erro parse JSON', e); }
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
      alternativaA: [''],
      alternativaB: [''],
      alternativaC: [''],
      alternativaD: [''],
      alternativaE: [''],
      respostaCorretaSelecionada: [''],
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

  bloquearCaracteresNaoNumericos(event: KeyboardEvent) {
    const teclasProibidas = ['e', 'E', '+', '-'];
    if (teclasProibidas.includes(event.key)) {
      event.preventDefault();
    }
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // getRawValue() é importante aqui para pegar o cursoId mesmo estando disabled
    const formValue = this.form.getRawValue();
    const tipoGlobal = formValue.tipoAvaliacao;

    formValue.questoes = formValue.questoes.map((q: any, index: number) => {
      q.tipoQuestao = tipoGlobal;
      q.ordem = index + 1;

      if (tipoGlobal === 'OBJETIVA') {
        const opcoesObj = {
          A: q.alternativaA,
          B: q.alternativaB,
          C: q.alternativaC,
          D: q.alternativaD,
          E: q.alternativaE
        };
        q.opcoesResposta = JSON.stringify(opcoesObj);
        q.respostaCorreta = q.respostaCorretaSelecionada;
      } else {
        q.opcoesResposta = null;
        q.respostaCorreta = null; 
      }
      // Limpa auxiliares
      delete q.alternativaA; delete q.alternativaB; delete q.alternativaC;
      delete q.alternativaD; delete q.alternativaE; delete q.respostaCorretaSelecionada;

      return q;
    });

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
        this.snackBar.open('Erro ao salvar.', 'Fechar');
      }
    });
  }
}