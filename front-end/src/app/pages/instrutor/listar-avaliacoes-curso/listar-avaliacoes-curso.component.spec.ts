import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarAvaliacoesCursoComponent } from './listar-avaliacoes-curso.component';

describe('ListarAvaliacoesCursoComponent', () => {
  let component: ListarAvaliacoesCursoComponent;
  let fixture: ComponentFixture<ListarAvaliacoesCursoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarAvaliacoesCursoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarAvaliacoesCursoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
