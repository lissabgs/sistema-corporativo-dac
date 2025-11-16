import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscricaoCursoComponent } from './inscricao-curso.component';

describe('InscricaoCursoComponent', () => {
  let component: InscricaoCursoComponent;
  let fixture: ComponentFixture<InscricaoCursoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscricaoCursoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscricaoCursoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
