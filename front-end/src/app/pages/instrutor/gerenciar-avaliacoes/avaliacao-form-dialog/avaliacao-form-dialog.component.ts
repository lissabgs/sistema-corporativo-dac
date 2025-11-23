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
import { AvaliacaoService } from '../../../../services/avaliacao.service';
import { Avaliacao } from '../../../../models/avaliacao.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

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

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<AvaliacaoFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { avaliacao?: Avaliacao, cursoIdPreSelecionado?: number }
  ) {
    this.form = this.fb.group({
      id: [null],
      codigo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: [''],
      cursoId: [null, Validators.required], // ID do curso obrigatório
      tempoLimiteMinutos: [60, [Validators.required, Validators.min(1)]],
      tentativasPermitidas: [3, [Validators.required, Validators.min(1)]],
      notaMinima: [70.0, [Validators.required, Validators.min(0), Validators.max(100)]],
      questoes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    if (this.data?.avaliacao) {
      this.isEditMode = true;
      this.carregarDados(this.data.avaliacao);
    } else if (this.data?.cursoIdPreSelecionado) {
      this.form.patchValue({ cursoId: this.data.cursoIdPreSelecionado });
    }
  }

  get questoes(): FormArray {
    return this.form.get('questoes') as FormArray;
  }

  carregarDados(avaliacao: Avaliacao) {
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

  criarQuestaoGroup(dados?: any): FormGroup {
    return this.fb.group({
      id: [dados?.id || null],
      tipoQuestao: [dados?.tipoQuestao || 'OBJETIVA', Validators.required],
      enunciado: [dados?.enunciado || '', Validators.required],
      opcoesResposta: [dados?.opcoesResposta || ''], // Ex: "A) Sim; B) Não"
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

    const avaliacao: Avaliacao = this.form.getRawValue();

    if (this.isEditMode && avaliacao.id) {
      this.avaliacaoService.atualizar(avaliacao.id, avaliacao).subscribe({
        next: (res) => {
          this.snackBar.open('Avaliação atualizada!', 'OK', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (err) => this.snackBar.open('Erro ao atualizar.', 'Fechar')
      });
    } else {
      this.avaliacaoService.criar(avaliacao).subscribe({
        next: (res) => {
          this.snackBar.open('Avaliação criada!', 'OK', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (err) => this.snackBar.open('Erro ao criar.', 'Fechar')
      });
    }
  }
}