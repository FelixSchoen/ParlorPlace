import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LobbyComponent } from './lobby.component';
import {Player} from "../../dto/player";
import {Game} from "../../dto/game";

describe('LobbyComponent', () => {
  let component: LobbyComponent<Game, Player>;
  let fixture: ComponentFixture<LobbyComponent<Game, Player>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LobbyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LobbyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
