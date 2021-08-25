import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WerewolfPlayerListComponent} from './werewolf-player-list.component';

describe('WerewolfPlayerListComponent', () => {
  let component: WerewolfPlayerListComponent;
  let fixture: ComponentFixture<WerewolfPlayerListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfPlayerListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfPlayerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
