import {UserRole} from "../enums/userrole";
import {GameType} from "../enums/gametype";

export class Game {
  constructor(public id: number,
              public gameType: GameType,
              public startedAt: string,
              public endedAt: string,
              public gameIdentifier: GameIdentifier) {
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
