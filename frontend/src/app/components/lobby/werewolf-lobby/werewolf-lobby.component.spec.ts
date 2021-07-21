import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WerewolfLobbyComponent } from './werewolf-lobby.component';

describe('WerewolfLobbyComponent', () => {
  let component: WerewolfLobbyComponent;
  let fixture: ComponentFixture<WerewolfLobbyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WerewolfLobbyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WerewolfLobbyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
