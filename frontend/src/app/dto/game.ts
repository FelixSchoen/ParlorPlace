import {GameType} from "../enums/gametype";
import {GameState} from "../enums/gamestate";
import {Player, WerewolfPlayer} from "./player";

export abstract class Game {
  protected constructor(public id: number,
                        public gameType: GameType,
                        public gameState: GameState,
                        public players: Set<Player>,
                        public startedAt: string,
                        public endedAt: string | null,
                        public gameIdentifier: GameIdentifier) {
  }
}

export class WerewolfGame extends Game {
  constructor(public id: number,
              public gameState: GameState,
              public players: Set<WerewolfPlayer>,
              public startedAt: string,
              public endedAt: string | null,
              public gameIdentifier: GameIdentifier) {
    super(id, GameType.WEREWOLF, gameState, players, startedAt, endedAt, gameIdentifier);
  }
}

export class GameIdentifier {
  constructor(public token: string) {
  }
}

export class GameStartRequest {
  constructor(public gameType: GameType) {
  }
}
