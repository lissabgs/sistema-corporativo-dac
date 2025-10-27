import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        LoginComponent, // componente standalone
        NoopAnimationsModule // evita erros com Angular Material nos testes
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form when empty', () => {
    expect(component.formLogin.valid).toBeFalse();
  });

  it('should validate form when fields are filled', () => {
    component.formLogin.setValue({ usuario: 'admin', senha: '1234' });
    expect(component.formLogin.valid).toBeTrue();
  });
});
