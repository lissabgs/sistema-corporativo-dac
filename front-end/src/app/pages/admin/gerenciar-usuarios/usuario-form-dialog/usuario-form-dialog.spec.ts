import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of } from 'rxjs';

// Importando a classe correta
import { UsuarioFormDialogComponent } from './usuario-form-dialog';
import { DepartamentoService } from '../../../../services/departamento.service';

describe('UsuarioFormDialogComponent', () => {
  let component: UsuarioFormDialogComponent;
  let fixture: ComponentFixture<UsuarioFormDialogComponent>;

  // Mock simples do serviço para não precisar de HTTP real no teste
  const departamentoServiceMock = {
    listarDepartamentos: () => of([])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioFormDialogComponent], // Importa o componente standalone
      providers: [
        // Fornece os valores que o construtor exige
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: DepartamentoService, useValue: departamentoServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuarioFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});