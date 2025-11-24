import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorrigirAvaliacaoComponent } from './corrigir-avaliacao.component';

describe('CorrigirAvaliacaoComponent', () => {
  let component: CorrigirAvaliacaoComponent;
  let fixture: ComponentFixture<CorrigirAvaliacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorrigirAvaliacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CorrigirAvaliacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
