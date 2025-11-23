import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenciarDepartamentos } from './gerenciar-departamentos';

describe('GerenciarDepartamentos', () => {
  let component: GerenciarDepartamentos;
  let fixture: ComponentFixture<GerenciarDepartamentos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerenciarDepartamentos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GerenciarDepartamentos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
