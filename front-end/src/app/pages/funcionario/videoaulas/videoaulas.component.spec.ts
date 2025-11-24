import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoaulasComponent } from './videoaulas.component';

describe('VideoaulasComponent', () => {
  let component: VideoaulasComponent;
  let fixture: ComponentFixture<VideoaulasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoaulasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideoaulasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
