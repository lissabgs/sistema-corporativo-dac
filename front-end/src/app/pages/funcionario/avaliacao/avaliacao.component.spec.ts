import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RealizarAvaliacaoComponent } from './avaliacao.component';

describe('RealizarAvaliacaoComponent', () => {
  let component: RealizarAvaliacaoComponent;
  let fixture: ComponentFixture<RealizarAvaliacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RealizarAvaliacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RealizarAvaliacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
