import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartamentoFormDialog } from './departamento-form-dialog';

describe('DepartamentoFormDialog', () => {
  let component: DepartamentoFormDialog;
  let fixture: ComponentFixture<DepartamentoFormDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartamentoFormDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DepartamentoFormDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
