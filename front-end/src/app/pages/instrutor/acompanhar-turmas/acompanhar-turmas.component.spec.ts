import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcompanharTurmasComponent } from './acompanhar-turmas.component';

describe('AcompanharTurmasComponent', () => {
  let component: AcompanharTurmasComponent;
  let fixture: ComponentFixture<AcompanharTurmasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcompanharTurmasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcompanharTurmasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
