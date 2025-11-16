import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CatalogoCursosComponent } from './catalogo-cursos.component';

describe('CatalogoCursosComponent', () => {
  let component: CatalogoCursosComponent;
  let fixture: ComponentFixture<CatalogoCursosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoCursosComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(CatalogoCursosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
