import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-matricula-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="dialog-container">
      <div class="dialog-header">
        <h2>Confirmar Inscrição</h2>
      </div>
      
      <mat-dialog-content class="dialog-content">
        <div class="icon-box">
          <mat-icon>school</mat-icon>
        </div>
        <p>Você deseja se matricular no curso:</p>
        <h3 class="curso-titulo">{{ data.titulo }}</h3>
        <p class="aviso">Ao confirmar, este curso aparecerá na sua lista de "Meus Cursos".</p>
      </mat-dialog-content>

      <mat-dialog-actions align="end" class="dialog-actions">
        <button mat-button mat-dialog-close>Cancelar</button>
        <button mat-raised-button color="primary" [mat-dialog-close]="true">
          <mat-icon>check_circle</mat-icon> Confirmar Matrícula
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-header {
      padding: 20px 24px;
      border-bottom: 1px solid #eee;
      background-color: #fff;
    }
    .dialog-header h2 { margin: 0; color: #0d47a1; font-size: 22px; }

    .dialog-content {
      padding: 30px 24px !important;
      text-align: center;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .icon-box {
      width: 60px; height: 60px;
      background-color: #e3f2fd;
      border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      margin-bottom: 15px;
    }
    .icon-box mat-icon { font-size: 32px; width: 32px; height: 32px; color: #0d47a1; }

    .curso-titulo { font-size: 18px; color: #333; margin: 10px 0; font-weight: 600; }
    .aviso { font-size: 12px; color: #777; margin-top: 5px; }

    .dialog-actions {
      padding: 15px 24px;
      border-top: 1px solid #eee;
      background-color: #fafafa;
    }
  `]
})
export class MatriculaDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<MatriculaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { titulo: string }
  ) {}
}