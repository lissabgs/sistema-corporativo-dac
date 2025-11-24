import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MinhasAvaliacoesComponent } from './minhas-avaliacoes.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('MinhasAvaliacoesComponent', () => {
  let component: MinhasAvaliacoesComponent;
  let fixture: ComponentFixture<MinhasAvaliacoesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MinhasAvaliacoesComponent,
        HttpClientTestingModule,
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MinhasAvaliacoesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve criar o componente com sucesso', () => {
    expect(component).toBeTruthy();
  });
});
