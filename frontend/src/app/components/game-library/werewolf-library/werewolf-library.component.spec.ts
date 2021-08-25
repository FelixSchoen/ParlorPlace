import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WerewolfLibraryComponent} from './werewolf-library.component';

describe('WerewolfLibraryComponent', () => {
  let component: WerewolfLibraryComponent;
  let fixture: ComponentFixture<WerewolfLibraryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfLibraryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
