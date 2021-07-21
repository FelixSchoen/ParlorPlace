import { TestBed } from '@angular/core/testing';

import { GameService } from './game.service';
import {Player} from "../dto/player";
import {Game} from "../dto/game";

describe('GameService', () => {
  let service: GameService<Game>;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
